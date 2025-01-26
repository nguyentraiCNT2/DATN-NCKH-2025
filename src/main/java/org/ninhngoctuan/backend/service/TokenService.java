package org.ninhngoctuan.backend.service;


import org.ninhngoctuan.backend.dto.TokenDTO;

public interface TokenService {
    void  saveToken(String token);
    void  logoutToken(String token);
    void getByid(Long id);
    Boolean validateToken(String token);
    Boolean getByToken(String token);
     void deleteExpiredTokens();
}
