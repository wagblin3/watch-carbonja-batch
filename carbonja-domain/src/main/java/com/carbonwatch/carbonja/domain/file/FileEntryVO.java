package com.carbonwatch.carbonja.domain.file;

public class FileEntryVO {

    private String key;

    private final long contentLength;

    public FileEntryVO(final String key, final long contentLength) {
        super();
        this.key = key;
        this.contentLength = contentLength;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getContentLength() {
        return contentLength;
    }

}
