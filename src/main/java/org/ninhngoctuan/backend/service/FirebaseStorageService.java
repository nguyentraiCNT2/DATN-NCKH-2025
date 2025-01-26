package org.ninhngoctuan.backend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    public static Storage storage;

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(getCredentials())
                    .build();

            FirebaseApp.initializeApp(options);

            // Khởi tạo Storage
            this.storage = StorageOptions.newBuilder()
                    .setCredentials(getCredentials())
                    .build()
                    .getService();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GoogleCredentials getCredentials() throws IOException {
        try (FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json")) {
            return GoogleCredentials.fromStream(serviceAccount);
        }
    }

    // Phương thức tải file lên Firebase Storage
    public String uploadFile(MultipartFile file) throws IOException {
        String randomPrefix = UUID.randomUUID().toString();
        String originalFileName = file.getOriginalFilename();
        String newFileName = randomPrefix + "_" + originalFileName; // Thêm chuỗi ngẫu nhiên vào tên file

        BlobId blobId = BlobId.of("fileuploads-161c3.appspot.com", newFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Tải file lên Firebase Storage
        Blob blob = storage.create(blobInfo, file.getBytes());

        // Tạo token mới
        String token = java.util.UUID.randomUUID().toString();

        // Cập nhật metadata của blob với token
        BlobInfo updatedBlobInfo = BlobInfo.newBuilder(blob.getBlobId())
                .setContentType(file.getContentType())
                .setMetadata(java.util.Collections.singletonMap("firebaseStorageDownloadTokens", token))
                .build();

        // Cập nhật blob với metadata mới
        storage.update(updatedBlobInfo);

        // Trả về URL của file cùng với token
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                "fileuploads-161c3.appspot.com",
                newFileName,
                token);
    }
}
