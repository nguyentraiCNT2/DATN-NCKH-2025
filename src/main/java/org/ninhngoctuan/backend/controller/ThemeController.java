package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.dto.RoomDTO;
import org.ninhngoctuan.backend.dto.ThemmeDTO;
import org.ninhngoctuan.backend.service.ThemeSevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/theme")
public class ThemeController {
    private final ThemeSevice themeSevice;


    public ThemeController(ThemeSevice themeSevice) {
        this.themeSevice = themeSevice;
    }

    // Lấy tất cả themes
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try {
            List<ThemmeDTO> themmeDTOList = themeSevice.getAllTheme();
            return ResponseEntity.ok(themmeDTOList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy theme theo ID
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ThemmeDTO themmeDTO = themeSevice.getThemeById(id);
            return ResponseEntity.ok(themmeDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Tạo theme mới
    @PostMapping("/create")
    public ResponseEntity<?> createTheme(@RequestParam("name") String name,
                                         @RequestParam("color") String color,
                                         @RequestParam("description") String description,
                                         @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            ThemmeDTO themmeDTO = new ThemmeDTO();
            themmeDTO.setColor(color);
            themmeDTO.setName(name);
            themmeDTO.setDescription(description);
            themeSevice.createTheme(themmeDTO, image);
            return ResponseEntity.ok("Theme created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật theme
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTheme(@PathVariable Long id,
                                         @RequestParam("name") String name,
                                         @RequestParam("color") String color,
                                         @RequestParam("description") String description,
                                         @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            ThemmeDTO themmeDTO = new ThemmeDTO();
            themmeDTO.setColor(color);
            themmeDTO.setName(name);
            themmeDTO.setDescription(description);
            themeSevice.updateTheme(id, themmeDTO,image);
            return ResponseEntity.ok("Theme updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa theme
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTheme(@PathVariable Long id) {
        try {
            themeSevice.deleteTheme(id);
            return ResponseEntity.ok("Theme deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Thay đổi theme cho phòng chat
    @PutMapping("/room/{roomId}/theme/{themeId}")
    public ResponseEntity<?> changeThemeByRoom(@PathVariable Long roomId, @PathVariable Long themeId) {
        try {
            themeSevice.changeThemeByRoom(roomId, themeId);
            return ResponseEntity.ok("Theme changed for room successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/room-detail/{id}")
    public ResponseEntity<?> getRoomDetail(@PathVariable Long id){
        try {
            RoomDTO dto = themeSevice.getRoomById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}