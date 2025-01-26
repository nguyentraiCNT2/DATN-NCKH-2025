package org.ninhngoctuan.backend.controller.output;

public class AuthStatus {
    private boolean isAuthenticated;
    private String role;

    public AuthStatus(boolean isAuthenticated, String role) {
        this.isAuthenticated = isAuthenticated;
        this.role = role;
    }
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
