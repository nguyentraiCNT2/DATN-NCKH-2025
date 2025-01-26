package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import org.ninhngoctuan.backend.entity.UserEntity;

public class RoomDTO {
    private  Long id;
    private UserDTO member1;
    private UserDTO member2;
    private String theme;
    private String themeCore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getMember1() {
        return member1;
    }

    public void setMember1(UserDTO member1) {
        this.member1 = member1;
    }

    public UserDTO getMember2() {
        return member2;
    }

    public void setMember2(UserDTO member2) {
        this.member2 = member2;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getThemeCore() {
        return themeCore;
    }

    public void setThemeCore(String themeCore) {
        this.themeCore = themeCore;
    }
}
