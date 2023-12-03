package com.clothes.websitequanao.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    String uploadFile(File file, String fileName) throws IOException; // used to upload a file

    File convertToFile(MultipartFile multipartFile, String fileName); // used to convert MultipartFile to File

    String getExtension(String fileName);  // used to get extension of a uploaded file

    Object upload(MultipartFile multipartFile);

    boolean deleteFile(String... fileName);
}
