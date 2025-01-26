package org.ninhngoctuan.backend.service.impl;


import jakarta.transaction.Transactional;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.TokenDTO;
import org.ninhngoctuan.backend.entity.TokenEntity;
import org.ninhngoctuan.backend.repository.TokensRepository;
import org.ninhngoctuan.backend.securityConfig.JwtTokenUtil;
import org.ninhngoctuan.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class TokenServiceIMPL implements TokenService {
    @Autowired
    private TokensRepository tokenRepository;
    @Override
    public void saveToken(String token) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setCreatedAt(new Date());
        RequestContext context = RequestContext.get();
        long oneHourInMillis = 1000 * 60 * 60 *24;
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + oneHourInMillis);
        tokenEntity.setExpiresAt(expiresAt);
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(true);
        tokenEntity.setUserId(context.getUserId());
        tokenRepository.save(tokenEntity);
    }



    @Override
    public void logoutToken(String token) {
        List<TokenEntity> checktoken = tokenRepository.findByToken(token);
        for (TokenEntity item : checktoken) {
            item.setExpiresAt(new Timestamp(System.currentTimeMillis()));
            item.setRevoked(false);
            tokenRepository.save(item);

        }

    }


    @Override
    public void getByid(Long id) {
        List<TokenEntity> checktoken = tokenRepository.findByUserId(id);
        for (TokenEntity item : checktoken) {
            item.setExpiresAt(new Timestamp(System.currentTimeMillis()));
            item.setRevoked(false);
            tokenRepository.save(item);
        }
    }

    @Override
    public Boolean validateToken(String token) {
        List<TokenEntity> checktoken = tokenRepository.findByToken(token);
        for (TokenEntity item : checktoken) {
            if (item.getExpiresAt().before(new Timestamp(System.currentTimeMillis())) && item.getRevoked() == false) return true;
        }
        return false;
    }

    @Override
    public Boolean getByToken(String token) {
         Boolean tokenEntities = tokenRepository.existsByToken(token);
        return tokenEntities;
    }
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    @Override
    public void deleteExpiredTokens() {
        Date currentDate = new Date();
        tokenRepository.deleteByExpiresAtBefore(currentDate);
    }
}
