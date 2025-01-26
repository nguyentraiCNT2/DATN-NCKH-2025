package org.ninhngoctuan.backend.controller.input;

import org.ninhngoctuan.backend.dto.TagDTO;

import java.util.List;

public class PostInput {
    private String content;
    private List<TagDTO> tagDTOS;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TagDTO> getTagDTOS() {
        return tagDTOS;
    }

    public void setTagDTOS(List<TagDTO> tagDTOS) {
        this.tagDTOS = tagDTOS;
    }
}
