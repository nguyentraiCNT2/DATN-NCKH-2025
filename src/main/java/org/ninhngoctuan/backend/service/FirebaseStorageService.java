package org.ninhngoctuan.backend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private static final String SUPABASE_URL = "https://xpmqijusgaglgiwoqliw.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhwbXFpanVzZ2FnbGdpd29xbGl3Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczODUxNTc3OCwiZXhwIjoyMDU0MDkxNzc4fQ.UC3gXJUgiurdwC2QEs812_adOu7Y-3ssujOCT7GDnIU";  // Thay bằng Service Role API Key
    private static final String BUCKET_NAME = "file_uploads";  // Tên bucket của bạn

    private final OkHttpClient client = new OkHttpClient();

    // Tải file lên Supabase (Dùng REST API của Supabase)
    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + uniqueFileName;

        RequestBody requestBody = RequestBody.create(file.getBytes(), MediaType.parse(file.getContentType()));

        // Sử dụng Bearer token trong header cho REST API yêu cầu xác thực
        Request request = new Request.Builder()
                .url(uploadUrl)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)  // Thêm Bearer Token
                .header("Content-Type", file.getContentType())  // Đảm bảo Content-Type chính xác
                .put(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.out.println("Lỗi khi tải lên Supabase: " + response.body().string());
            throw new IOException("Lỗi tải file lên Supabase: " + response.message());
        }

        return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET_NAME + "/" + uniqueFileName;
    }
}
