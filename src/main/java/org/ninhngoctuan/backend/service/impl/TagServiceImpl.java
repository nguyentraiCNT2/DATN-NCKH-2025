package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.dto.TagDTO;
import org.ninhngoctuan.backend.entity.TagEntity;
import org.ninhngoctuan.backend.repository.TagRepository;
import org.ninhngoctuan.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TagDTO> getAll() {
        List<TagDTO>  list = new ArrayList<>();
        List<TagEntity> tagEntities = tagRepository .findAll();
        if (tagEntities.size() ==0)
            throw new RuntimeException("Không có nhãn nào");
        for (TagEntity tagEntity: tagEntities){
            TagDTO tagDTO = modelMapper.map(tagEntity, TagDTO.class);
            list.add(tagDTO);
        }
        return list;
    }

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        try {
            if (tagDTO.getName() == null || tagDTO.getName() == "")
                throw new RuntimeException("Tên nhãn không thể bỏ trống");
            TagEntity tagEntity = modelMapper.map(tagDTO, TagEntity.class);
           TagEntity save_tag =  tagRepository.save(tagEntity);
           return modelMapper.map(save_tag, TagDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public TagDTO updateTag(TagDTO tagDTO) {
        try {
            if (tagDTO.getName() == null || tagDTO.getName() == "")
                throw new RuntimeException("Tên nhãn không thể bỏ trống");
            TagEntity tag  =tagRepository.findById(tagDTO.getTagId()).orElseThrow(() -> new RuntimeException("Không có nhãn nào có id là: "+tagDTO.getTagId()));
            tag.setName(tagDTO.getName());
            TagEntity save_tag = tagRepository.save(tag);
            return modelMapper.map(save_tag, TagDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteTag(Long tagid) {
            TagEntity tag = tagRepository.findById(tagid).orElseThrow(() -> new RuntimeException("Không tim thấy nhãn có id là: "+tagid));
            tagRepository.delete(tag);
    }
}
