package com.agnext.qualixfarmer.network.Response;

public class LoginRequest {

    private String device_token;

    public LoginRequest(String device_token) {
        this.device_token = device_token;
    }
}