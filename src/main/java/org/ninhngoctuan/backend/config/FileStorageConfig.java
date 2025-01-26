package org.ninhngoctuan.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {
    @Value("${images.dir}")
    private String imagesDir;
    @Value("${videos.dir}")
    private String videosDir;

    public String getImagesDir() {
        return imagesDir;
    }

    public String getVideosDir() {
        return videosDir;
    }

}
