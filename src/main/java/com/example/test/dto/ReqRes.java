package com.example.test.dto;

import com.example.test.entity.OurUsers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {
    private int statusCode;
    private String username;
    private String name;
    private String surName;
    private String email;
    private String telNumber;
    private LocalDate birthdate;
    private String password;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String shotName;
    private Double balance;
    private String enabled;
    private String code;
    private List<OurUsers> data;
    public void setData(List<OurUsers> data) {
        this.data = data;
    }
}
