package org.ninhngoctuan.backend.config;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private int status;

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setStatus(int status) {
        super.setStatus(status);
        this.status = status;
    }


    @Override
    public int getStatus() {
        return this.status;
    }
}
