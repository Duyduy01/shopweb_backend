package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.service.FileService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {
    private static String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/website-83b41.appspot.com/o/%s?alt=media";
    private Storage storage;

    private static final String BUCKET = "website-83b41.appspot.com";

    public FileServiceImpl() throws IOException {
        ClassPathResource resource = new ClassPathResource("firebase.json");

//        FileInputStream serviceAccount = new FileInputStream(resource.getFilename());

        Credentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

        this.storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build().getService();
    }

    @Override
    public String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));
    }

    @Override
    public File convertToFile(MultipartFile multipartFile, String fileName) {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    @Override
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public Object upload(MultipartFile multipartFile) {
        String TEMP_URL = "";
        File file = null;
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link

            return TEMP_URL;                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (file != null) file.delete();   // to delete the copy of uploaded file stored in the project folder
        }
    }

    @Override
    public boolean deleteFile(String... fileName) {
        try {
            List<BlobId> blobIds = new ArrayList<>();
            for (String name : fileName) {
                BlobId blobId = BlobId.of(BUCKET, name);
                blobIds.add(blobId);
            }
            storage.delete(blobIds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}