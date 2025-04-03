package com.bal.bal_management_system.service;

import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile file, String fileName) {
        try {
            // Upload file to Firebase Storage
            StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType());
            // Return the public download URL
            return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    StorageClient.getInstance().bucket().getName(),
                    fileName.replace("/", "%2F")); // Encode the file name
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
