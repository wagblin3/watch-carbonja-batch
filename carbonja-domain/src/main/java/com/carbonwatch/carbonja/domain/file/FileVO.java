package com.carbonwatch.carbonja.domain.file;

public class FileVO extends FileEntryVO {
    private final String contentType;

    public FileVO(final String key, final String contentType, final long contentLength) {
        super(key, contentLength);
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

}
