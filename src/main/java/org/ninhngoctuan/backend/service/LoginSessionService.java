package org.ninhngoctuan.backend.service;


import org.ninhngoctuan.backend.dto.LoginSessionDTO;

public interface LoginSessionService {
    LoginSessionDTO createAndCheckLoginSession(Long userId, String ipAddress, String hostName, String useAgent);
}
