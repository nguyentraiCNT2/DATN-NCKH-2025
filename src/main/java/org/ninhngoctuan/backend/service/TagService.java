package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.TagDTO;

import java.util.List;

public interface TagService {
    List<TagDTO> getAll();
    TagDTO createTag(TagDTO tagDTO);
    TagDTO updateTag(TagDTO tagDTO);
    void deleteTag(Long tagid);
}
