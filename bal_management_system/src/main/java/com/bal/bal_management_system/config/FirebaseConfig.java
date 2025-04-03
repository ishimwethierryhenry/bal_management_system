//package com.bal.bal_management_system.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class FirebaseConfig {
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
//            InputStream serviceAccount = resource.getInputStream();
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setStorageBucket("bal-management-system-e8472.appspot.com")
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to initialize Firebase", e);
//        }
//    }
//}
//
