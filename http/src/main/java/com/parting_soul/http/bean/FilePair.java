package com.parting_soul.http.bean;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public class FilePair {
    private String filePath;
    private byte[] data;

    public FilePair(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
