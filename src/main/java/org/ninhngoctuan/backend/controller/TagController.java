package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.dto.TagDTO;
import org.ninhngoctuan.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/getall")
    public ResponseEntity<?> getAll(){
        try {
            List<TagDTO> tagDTOS = tagService.getAll();
            return  ResponseEntity.status(HttpStatus.OK).body(tagDTOS);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTag(@RequestBody  TagDTO tagDTO){
        try {
            TagDTO dto = tagService.createTag(tagDTO);
            return  ResponseEntity.status(HttpStatus.OK).body(dto);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO){
        try {
            tagDTO.setTagId(id);
            TagDTO dto = tagService.updateTag(tagDTO);
            return  ResponseEntity.status(HttpStatus.OK).body(dto);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id){
        try {
            tagService.deleteTag(id);
            return  ResponseEntity.status(HttpStatus.OK).body("Xóa thành công");
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
