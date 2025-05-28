package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.RoomDTO;
import org.ninhngoctuan.backend.dto.ThemmeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ThemeSevice {
    List<ThemmeDTO> getAllTheme();
    ThemmeDTO getThemeById(Long id);
    void createTheme(ThemmeDTO themmeDTO, MultipartFile image);
    void updateTheme(Long id,ThemmeDTO themmeDTO, MultipartFile image);
    void deleteTheme(Long id);
    void changeThemeByRoom(Long roomId, Long themeId);
    void deleteThemeByRoom(Long roomId);
    RoomDTO getRoomById(Long id);
}
