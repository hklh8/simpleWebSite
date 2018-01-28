package com.hklh8.controller;

import com.hklh8.dto.FileDto;
import com.hklh8.service.FileService;
import com.hklh8.utils.ResponseUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GouBo on 2018/1/28.
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    /**
     * 上传文件(支持同时传多个文件)
     */
    @ResponseBody
    @PostMapping("/uploadImage")
    public Object uploadImage(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            return fileService.uploadImage(multiRequest);
        }
        return ResponseUtils.formatError("803", "上传失败！", "检查form中是否有enctype=\"multipart/form-data\"");
    }

    /**
     * 文件下载
     */
    @RequestMapping("/downloadImage/{fileId:[a-z0-9]{32}}")
    public void downloadImage(@PathVariable String fileId, HttpServletResponse response) throws IOException {
        FileDto fileDto = fileService.getFileDtoById(fileId);
        if (fileDto == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(ResponseUtils.formatError("803", "文件不存在！", "请检查参数(fileId).").toJSONString());
            return;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileDto.getFileName());

        try (InputStream inputStream = new FileInputStream(fileDto.getFilePath());
             OutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
    }

    @ResponseBody
    @GetMapping("/getFiles")
    public Object getFiles() {
        List<FileDto> FileDtos = new ArrayList<>();
        Map<String, FileDto> allFileDto = fileService.readDataFromFile();
        for (String str : allFileDto.keySet()) {
            FileDto fileDto = allFileDto.get(str);
            fileDto.setFilePath(null);
            FileDtos.add(fileDto);
        }
        return FileDtos;
    }

}
