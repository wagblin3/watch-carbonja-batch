package com.carbonwatch.carbonja.domain.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * File repository agnostic to the technology used to persist file.
 */
public interface FileRepository {

    /**
     * Delete file from the repository
     *
     * @param fileName
     *            the name of the file to delete
     */
    void deleteFile(String fileName);

    /**
     * Save file to the repository
     *
     * @param fileName
     *            the name of the file used later to retrieve it.
     * @param file
     *            the file content stream to save
     * @param size
     *            the size of the file
     * @param contentType
     *            the content type used for metadatas
     * @throws IOException
     */
    void saveFile(String fileName, InputStream file, long size, String contentType);

    /**
     * Get meta data of the object according to the given key name.
     *
     * @param keyName
     * @return
     */
    FileVO getMetaData(String keyName);

    /**
     * Load file from the repository.
     *
     * @param fileName
     * @param outputstream
     */
    void loadFile(String fileName, OutputStream outputstream);

    /**
     * Move file in the repository
     *
     * @param keyName
     *            name of the origin object
     * @param directoryDest
     *            name of the destination object
     */
    void moveFile(String keyName, String directoryDest);

    /**
     * List Storage content
     */
    List<FileEntryVO> listObjects();


}
