package com.hklh8.service;

import com.hklh8.dto.FileDto;
import com.hklh8.utils.FileUtils;
import com.hklh8.utils.ResponseUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GouBo on 2018/1/28.
 */
@Service
public class FileService {

    @Value("${data.file}")
    String path;

    @Value("${image.path}")
    String imagePath;

    public Object uploadImage(MultipartHttpServletRequest multiRequest) {
        Map<String, FileDto> fileDtos = new HashMap<>();
        Map<String, MultipartFile> fileMap = multiRequest.getFileMap();

        for (String paramName : fileMap.keySet()) {
            MultipartFile file = fileMap.get(paramName);
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                if (!FileUtils.checkFile(fileName)) {
                    return ResponseUtils.formatError("801", "文件格式不正确！", "只能上传图片格式！");
                }
                String uuid = FileUtils.getGUID();
                String newFileName = uuid + "." + FileUtils.getExtensionName(fileName);
                String filePath = new File(imagePath).getAbsolutePath() + File.separator + newFileName;
                File targetFile = new File(filePath);
                try {
                    file.transferTo(targetFile);
                    fileDtos.put(paramName, new FileDto(paramName, fileName, uuid, filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseUtils.formatError("802", "文件上传出错！", e.getMessage());
                }
            }
        }
        saveDataToFile(fileDtos);
        List<FileDto> result = new ArrayList<>();
        for (String paramName : fileDtos.keySet()) {
            FileDto fileDto = fileDtos.get(paramName);
            fileDto.setFilePath(null);
            result.add(fileDto);
        }
        return result;
    }


    public FileDto getFileDtoById(String id) {
        FileDto fileDto = null;
        Map<String, FileDto> allFileDto = readDataFromFile();
        for (String paramName : allFileDto.keySet()) {
            FileDto temp = allFileDto.get(paramName);
            if (id.equals(temp.getUuid())) {
                fileDto = temp;
                break;
            }
        }
        return fileDto;
    }

    /**
     * 保存数据到文件
     */
    private void saveDataToFile(Map<String, FileDto> fileDtos) {
        Map<String, FileDto> allFileDto = readDataFromFile();
        allFileDto.putAll(fileDtos);

        String absolutePath = new File(path).getAbsolutePath();
        StringBuilder strb = new StringBuilder();
        for (String paramName : allFileDto.keySet()) {
            FileDto fileDto = allFileDto.get(paramName);
            strb.append(fileDto.getParamName()).append("\t")
                    .append(fileDto.getFileName()).append("\t")
                    .append(fileDto.getUuid()).append("\t")
                    .append(fileDto.getFilePath()).append("\t")
                    .append("\r\n");
        }
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(absolutePath);
            IOUtils.write(strb.toString(), outStream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件读取数据
     */
    public Map<String, FileDto> readDataFromFile() {
        Map<String, FileDto> fileDtos = new HashMap<>();

        String absolutePath = new File(path).getAbsolutePath();
        File file = new File(absolutePath);
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            IOUtils.readFully(inputStream, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String content = new String(bytes, Charset.forName("UTF-8"));
        if (content.contains("\r\n") && content.contains("\t")) {
            String[] fileDtoValues = content.split("\r\n");
            for (String str : fileDtoValues) {
                String[] FieldValues = str.split("\t");
                fileDtos.put(FieldValues[0], new FileDto(FieldValues[0], FieldValues[1], FieldValues[2], FieldValues[3]));
            }
        }
        return fileDtos;
    }

}
