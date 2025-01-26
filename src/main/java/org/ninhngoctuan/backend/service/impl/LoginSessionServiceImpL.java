package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.dto.LoginSessionDTO;
import org.ninhngoctuan.backend.entity.LoginSessionEntity;
import org.ninhngoctuan.backend.repository.LoginSessionRepository;
import org.ninhngoctuan.backend.service.LoginSessionService;
import org.ninhngoctuan.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LoginSessionServiceImpL implements LoginSessionService {
    private LoginSessionRepository loginSessionRepository;
    private TokenService tokenService;
    private ModelMapper modelMapper;
    @Autowired
    public LoginSessionServiceImpL(LoginSessionRepository loginSessionRepository, TokenService tokenService, ModelMapper modelMapper) {
        this.loginSessionRepository = loginSessionRepository;
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
    }


    @Override
    public LoginSessionDTO createAndCheckLoginSession(Long userId, String ipAddress, String hostName, String useAgent) {
        try {
            List<LoginSessionEntity > loginSessionEntities = loginSessionRepository.findByUserid(userId);
            for (LoginSessionEntity loginSessionEntity : loginSessionEntities) {
                if ( !loginSessionEntity.getIpAddress().equals(ipAddress)
                        || !loginSessionEntity.getHostName().equals(hostName)
                        || !loginSessionEntity.getUserAgent().equals(useAgent)) {

                    tokenService.getByid(loginSessionEntity.getUserid());
                    loginSessionRepository.delete(loginSessionEntity);
                }
            }
            LoginSessionEntity loginSessionEntity = new LoginSessionEntity();
            loginSessionEntity.setUserid(userId);
            loginSessionEntity.setIpAddress(ipAddress);
            loginSessionEntity.setHostName(hostName);
            loginSessionEntity.setUserAgent(useAgent);
            loginSessionEntity.setLoginTime(new Timestamp(System.currentTimeMillis()));
           LoginSessionEntity  saved =  loginSessionRepository.save(loginSessionEntity);
            return modelMapper.map(saved, LoginSessionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
