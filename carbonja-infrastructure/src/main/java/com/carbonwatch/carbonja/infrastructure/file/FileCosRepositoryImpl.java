package com.carbonwatch.carbonja.infrastructure.file;

import com.carbonwatch.carbonja.domain.common.exception.NotFoundException;
import com.carbonwatch.carbonja.domain.common.exception.TechnicalException;
import com.carbonwatch.carbonja.domain.file.FileEntryVO;
import com.carbonwatch.carbonja.domain.file.FileRepository;
import com.carbonwatch.carbonja.domain.file.FileVO;
import com.ibm.cloud.objectstorage.AmazonClientException;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import com.ibm.cloud.objectstorage.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({ "local", "cloud" })
public class FileCosRepositoryImpl extends FileBasicCosRepository implements FileRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileCosRepositoryImpl.class);

    public FileCosRepositoryImpl(final AmazonS3 s3Client) {
        super(s3Client);
    }

    @Override
    public void deleteFile(final String fileName) {
        final DeleteObjectRequest request = new DeleteObjectRequest( // request the new object by identifying
                bucketName, // the name of the bucket
                fileName // the name of the object
        );
        s3Client.deleteObject(request);
        LOGGER.info("File {} successfully  deleted", fileName);
    }

    @Override
    public void saveFile(final String fileName, final InputStream inputStream, final long fileSize, final String contentType) {
        saveSingleFile(fileName, inputStream, fileSize, contentType);
    }

    @Override
    public void loadFile(final String fileName, final OutputStream outputstream) {
        try {
            final S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
            IOUtils.copy(s3object.getObjectContent(), outputstream);
        } catch (final AmazonS3Exception asex) {
            throw new NotFoundException(asex);
        } catch (final IOException | AmazonClientException ex) {
            throw new TechnicalException(ex);
        }
    }

    @Override
    public void moveFile(final String fileName, final String directoryDest) {
        if (s3Client.doesObjectExist(bucketName, fileName)) {
            final CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, fileName, bucketName, directoryDest);
            s3Client.copyObject(copyObjRequest);
            LOGGER.info("Copy File {} into {} successfully", fileName, directoryDest);
        } else {
            LOGGER.info("The file {} does not exist ", fileName);
        }
    }

    @Override
    public List<FileEntryVO> listObjects() {
        LOGGER.info("Retrieving bucket contents from: {}", bucketName);

        final List<FileEntryVO> bucketListing = new ArrayList<>();

        final ListObjectsV2Result objectListing = s3Client.listObjectsV2(bucketName);
        for (final S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            bucketListing.add(new FileEntryVO(objectSummary.getKey(), objectSummary.getSize()));
            LOGGER.info("Item: {} ({} bytes)\n", objectSummary.getKey(), objectSummary.getSize());
        }

        return bucketListing;
    }

    @Override
    public FileVO getMetaData(String fileName) {
        final ObjectMetadata s3objectMetadata = s3Client.getObjectMetadata(new GetObjectMetadataRequest(bucketName, fileName));
        return new FileVO(fileName, s3objectMetadata.getContentType(), s3objectMetadata.getContentLength());
    }
}