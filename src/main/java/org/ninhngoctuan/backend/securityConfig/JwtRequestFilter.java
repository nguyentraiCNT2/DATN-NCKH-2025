package org.ninhngoctuan.backend.securityConfig;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.UUID;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    @Lazy
    private CustomUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TokenService tokenService;
//    @Autowired
//    private LogService logService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Create a wrapper to capture response status
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            private int status = HttpServletResponse.SC_OK;

            @Override
            public void setStatus(int status) {
                super.setStatus(status);
                this.status = status;
            }

            @Override
            public int getStatus() {
                return this.status;
            }
        };

        createRequestContext(request, responseWrapper);
        try {
            String jwtToken = extractJwtToken(request);
            if (jwtToken == null) {
                chain.doFilter(request, responseWrapper);
                return;
            }

            validateToken(jwtToken);

            String username = extractUsername(jwtToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(request, username, jwtToken);
            }

            chain.doFilter(request, responseWrapper);
        }finally {
            // Log the exception and set error status
            RequestContext requestContext = RequestContext.get();
            if (requestContext != null) {
                requestContext.setResponseStatus(responseWrapper.getStatus());
                requestContext.setRequestURL(request.getRequestURL().toString());
                requestContext.setMethod(request.getMethod());
            }

            RequestContext.clear();
        }
    }

    private void createRequestContext(HttpServletRequest request, HttpServletResponseWrapper responseWrapper) {
        RequestContext requestContext = new RequestContext();
        requestContext.setRequestId(UUID.randomUUID().toString());
        requestContext.setTimestamp(Instant.now());
        String requestURL = request.getRequestURL().toString();
        int statusCode = responseWrapper.getStatus();
        String httpMethod = request.getMethod();
        requestContext.setRequestURL(requestURL);
        requestContext.setMethod(httpMethod);
        requestContext.setResponseStatus(statusCode);
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        // Get Hostname
        String hostName = "unknown";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostName = inetAddress.getHostName();
        } catch (UnknownHostException e) {
            // Handle exception
        }

        String userAgent = request.getHeader("User-Agent");

        requestContext.setIpAddress(ipAddress);
        requestContext.setHostName(hostName);
        requestContext.setUserAgent(userAgent);

        RequestContext.set(requestContext);
    }

    private String extractJwtToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }
        return null;
    }

    private String extractUsername(String jwtToken) {
        try {
            return jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unable to get JWT Token", e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT Token has expired", e);
        }
    }

    private void validateToken(String jwtToken) {
        boolean redischecktoken = tokenService.validateToken(jwtToken);
        if (redischecktoken) {

            throw new RuntimeException("Bạn đã đăng xuất!");
        }
    }

    private void authenticateUser(HttpServletRequest request, String username, String jwtToken) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            throw new RuntimeException("Phiên đăng nhập của bạn đã kết thúc, vui lòng đăng nhập lại");
        }
    }
}
