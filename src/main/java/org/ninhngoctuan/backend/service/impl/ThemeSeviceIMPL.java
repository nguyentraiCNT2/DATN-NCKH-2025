package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.dto.RoomDTO;
import org.ninhngoctuan.backend.dto.ThemmeDTO;
import org.ninhngoctuan.backend.entity.RoomEntity;
import org.ninhngoctuan.backend.entity.ThemmeEntity;
import org.ninhngoctuan.backend.repository.*;
import org.ninhngoctuan.backend.service.FirebaseStorageService;
import org.ninhngoctuan.backend.service.ThemeSevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThemeSeviceIMPL implements ThemeSevice {
    private final RoomRepository roomRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final ModelMapper modelMapper;
    private final FirebaseStorageService firebaseStorageService;
    @Value("${images.dir}")
    private String imagesDir;
    public ThemeSeviceIMPL(RoomRepository roomRepository, ThemeRepository themeRepository, UserRepository userRepository, FriendsRepository friendsRepository, ModelMapper modelMapper, FirebaseStorageService firebaseStorageService) {
        this.roomRepository = roomRepository;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
        this.friendsRepository = friendsRepository;
        this.modelMapper = modelMapper;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public List<ThemmeDTO> getAllTheme() {
        try {
            List<ThemmeDTO> themmeDTOS = new ArrayList<>();
            List<ThemmeEntity> themes = themeRepository.findAll();
            themes.stream().forEach(themmeEntity -> {
                ThemmeDTO themmeDTO = modelMapper.map(themmeEntity, ThemmeDTO.class);
                themmeDTOS.add(themmeDTO);
            });
            return themmeDTOS;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public ThemmeDTO getThemeById(Long id) {
        try {
            ThemmeEntity themmeEntity = themeRepository.findById(id).orElseThrow(() -> new RuntimeException("Theme not found"));
                ThemmeDTO themmeDTO = modelMapper.map(themmeEntity, ThemmeDTO.class);
                return themmeDTO;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createTheme(ThemmeDTO themmeDTO , MultipartFile image) {
        try {
            ThemmeEntity themme = modelMapper.map(themmeDTO, ThemmeEntity.class);
            Path uploadPathImages = Paths.get(imagesDir);

            if (!Files.exists(uploadPathImages)) {
                Files.createDirectories(uploadPathImages);
            }

            if (image != null && !image.isEmpty()) {
                String imageFileName = firebaseStorageService.uploadFile(image);
                themme.setImage(imageFileName);
            }
            themme.setCreatedAt(Timestamp.from(Instant.now()));
            themeRepository.save(themme);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateTheme(Long id, ThemmeDTO themmeDTO, MultipartFile image) {
            try {
                ThemmeEntity themmeEntity = themeRepository.findById(id).orElseThrow(() -> new RuntimeException("Theme not found"));
                Path uploadPathImages = Paths.get(imagesDir);

                if (!Files.exists(uploadPathImages)) {
                    Files.createDirectories(uploadPathImages);
                }

                if (image != null && !image.isEmpty()) {
                    String imageFileName = firebaseStorageService.uploadFile(image);
                    themmeEntity.setImage(imageFileName);
                }
                themmeEntity.setColor(themmeDTO.getColor());
                themmeEntity.setName(themmeDTO.getName());
                themmeEntity.setUpdatedAt(Timestamp.from(Instant.now()));
                themeRepository.save(themmeEntity);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
    }

    @Override
    public void deleteTheme(Long id) {

    }

    @Override
    public void changeThemeByRoom(Long roomId, Long themeId) {
        try {
            RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
            ThemmeEntity themmeEntity = themeRepository.findById(themeId).orElseThrow(() -> new RuntimeException("Theme not found"));
            roomEntity.setThememeId(themmeEntity);
            roomRepository.save(roomEntity);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteThemeByRoom(Long roomId) {

    }

    @Override
    public RoomDTO getRoomById(Long id) {
        try {
            RoomEntity roomEntity = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
            return modelMapper.map(roomEntity, RoomDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
