package com.batab.blog.dto;

public class LikeRequest {
    private String email;

    public LikeRequest(){

    }
    public LikeRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
