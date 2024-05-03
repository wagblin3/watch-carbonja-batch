package com.carbonwatch.carbonja.infrastructure.file;

import com.carbonwatch.carbonja.domain.common.exception.TechnicalException;
import com.ibm.cloud.objectstorage.AmazonClientException;
import com.ibm.cloud.objectstorage.SdkClientException;
import com.ibm.cloud.objectstorage.services.kms.model.NotFoundException;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class FileBasicCosRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileBasicCosRepository.class);

    protected static final long PART_SIZE = 1024L * 1024L * 5; // Multiparts upload operations are recommended for objects larger than 5 MB

    protected final AmazonS3 s3Client;

    protected FileBasicCosRepository(final AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${application.bucket-name}")
    protected String bucketName;

    protected void verifyBucket() {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            throw new NotFoundException("Bucket " + bucketName + " doesnt exist");
        }
    }

    public void saveSingleFile(final String fileName, final InputStream inputStream, final long fileSize, final String contentType) {
        try {
            verifyBucket();
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(fileSize);

            // uploading the parts min 5MB part size
            if (fileSize > PART_SIZE) {
                uploadMultiPartFile(fileName, inputStream, fileSize, metadata);
            } else {
                uploadStandard(fileName, inputStream, metadata);
            }
            LOGGER.info("File {} successfully added ", fileName);

        } catch (final AmazonClientException ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Do the upload with the "standard" method used if file length < 5MB
     *
     * @param objectKey
     *            key of the object to retrieve it later
     * @param inputStream
     *            stream of the data to upload
     * @param metadata
     *            metadata of the uploaded object
     */
    private void uploadStandard(final String objectKey, final InputStream inputStream, final ObjectMetadata metadata) {
        final PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, objectKey, inputStream, metadata);
        s3Client.putObject(putObjectRequest);
    }

    /**
     * Do the upload with the "MultiPart" method used if file length > 5MB
     *
     * @param objectKey
     *            key of the object to retrieve it later
     * @param inputStream
     *            stream of the data to upload
     * @param fileSize
     *            size of the file to upload
     * @param metadata
     *            COS Object Metadata
     * @throws IOException
     */
    private void uploadMultiPartFile(final String objectKey, final InputStream inputStream, final long fileSize, final ObjectMetadata metadata) {
        final InitiateMultipartUploadResult mpResult = s3Client.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, objectKey, metadata));
        final String uploadID = mpResult.getUploadId();

        final long partCount = fileSize / PART_SIZE + 1;
        final List<PartETag> dataPacks = new ArrayList<>();
        long partSize = PART_SIZE;

        try {
            long position = 0;
            for (int partNum = 1; position < fileSize; partNum++) {
                partSize = Math.min(partSize, fileSize - position);

                LOGGER.info("Uploading to {} (part {} of {})", objectKey, partNum, partCount);
                final UploadPartRequest upRequest = new UploadPartRequest() //
                        .withBucketName(bucketName) //
                        .withKey(objectKey) //
                        .withUploadId(uploadID) //
                        .withPartNumber(partNum) //
                        .withInputStream(inputStream) //
                        .withPartSize(partSize).withObjectMetadata(metadata);

                final UploadPartResult upResult = s3Client.uploadPart(upRequest);
                dataPacks.add(upResult.getPartETag());
                position += partSize;
            }

            // complete upload
            s3Client.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, objectKey, uploadID, dataPacks));
            LOGGER.info("{} sucessfully uploaded", objectKey);
        } catch (final SdkClientException sdke) {
            LOGGER.warn("Multi-part upload aborted for {}", objectKey);
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectKey, uploadID));
            throw new TechnicalException(sdke.getMessage(), sdke);
        }
    }
}
