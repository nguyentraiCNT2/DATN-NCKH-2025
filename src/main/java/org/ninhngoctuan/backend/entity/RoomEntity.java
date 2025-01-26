package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @ManyToOne
    @JoinColumn(name = "member1")
    private UserEntity member1;
    @ManyToOne
    @JoinColumn(name = "member2")
    private UserEntity member2;
    private String theme;
    private String themeCore;
    @ManyToOne
    @JoinColumn(name = "templateId")
    private ThemmeEntity thememeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getMember1() {
        return member1;
    }

    public void setMember1(UserEntity member1) {
        this.member1 = member1;
    }

    public UserEntity getMember2() {
        return member2;
    }

    public void setMember2(UserEntity member2) {
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

    public ThemmeEntity getThememeId() {
        return thememeId;
    }

    public void setThememeId(ThemmeEntity thememeId) {
        this.thememeId = thememeId;
    }
}
