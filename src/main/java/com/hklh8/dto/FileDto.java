package com.hklh8.dto;

/**
 * Created by GouBo on 2018/1/28.
 */
public class FileDto {

    private String paramName;
    private String fileName;
    private String uuid;
    private String filePath;

    public FileDto(String paramName, String fileName, String uuid, String filePath) {
        this.paramName = paramName;
        this.fileName = fileName;
        this.uuid = uuid;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
