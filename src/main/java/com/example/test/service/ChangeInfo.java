package com.example.test.service;

import com.example.test.dto.ReqRes;
import com.example.test.entity.OurUsers;
import com.example.test.repository.OurUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChangeInfo {


    private final OurUserRepo ourUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final SendSmsService sendSmsService;
    private final JWTUtils jwtUtils;

    @Value("${account.confirm.base-url}")
    private String baseUrl;
    @Value("${server.port}")
    private String port;

    public ReqRes searchData(ReqRes info) {
        ReqRes response = new ReqRes();
        Set<OurUsers> resultSet = new HashSet<>();
        try {
            if (info.getEmail() != null) {
                resultSet.addAll(searchByEmail(info).getData());
            }
            if (info.getName() != null) {
                resultSet.addAll(searchByName(info).getData());
            }
            if (info.getBirthdate() != null) {
                resultSet.addAll(searchByBirthdate(info).getData());
            }
            if (info.getTelNumber() != null) {
                resultSet.addAll(searchByTelNumber(info).getData());
            }

            response.setData(new ArrayList<>(resultSet));
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes searchByBirthdate(ReqRes info) {
        ReqRes response = new ReqRes();
        try {
            List<OurUsers> users = ourUserRepo.findByBirthdate(info.getBirthdate());
            response.setData(users);
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes searchByName(ReqRes info) {
        ReqRes response = new ReqRes();
        try {
            List<OurUsers> users = ourUserRepo.findByNameStartingWith(info.getName());
            response.setData(users);
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes searchByEmail(ReqRes info) {
        ReqRes response = new ReqRes();
        try {
            OurUsers user = ourUserRepo.findByEmail(info.getEmail()).orElseThrow();
            response.setData(List.of(user)); // Wrap the single user in a list
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes searchByTelNumber(ReqRes info) {
        ReqRes response = new ReqRes();
        try {
            OurUsers user = ourUserRepo.findByTelNumber(info.getTelNumber()).orElseThrow();
            response.setData(List.of(user)); // Wrap the single user in a list
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes changeinfo(ReqRes info){
        ReqRes response = new ReqRes();
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            OurUsers ourUsers = ourUserRepo.findByUsername(username).orElseThrow();
            System.out.println(ourUsers);
            if(info.getEmail()!=null) {

                String token = jwtUtils.generateToken(ourUsers);
                String link = baseUrl + ":" + port + "/auth/confirm-account/" + token;
                sendSmsService.sendSms(info,link);
                ourUsers.setEnabled(false);
                ourUsers.setEmail(info.getEmail());
                ourUserRepo.save(ourUsers);
                response.setStatusCode(200);
                response.setMessage("Send sms to"+info.getEmail());

                return response;

            }
            if(info.getTelNumber() != null)
                ourUsers.setTelNumber(info.getTelNumber());
            ourUserRepo.save(ourUsers);
            response.setStatusCode(200);


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
        }

    public ReqRes delete(ReqRes info){
        ReqRes response = new ReqRes();
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            OurUsers ourUsers = ourUserRepo.findByUsername(userDetails.getUsername()).orElseThrow();
            System.out.println(ourUsers);

            // Проверяем, не равны ли email и номер телефона null перед удалением
            if (info.getEmail() != null && info.getEmail().equals("delete")) {
                System.out.println("1");
                ourUsers.setEmail(null);
            }
            if (info.getTelNumber() != null && info.getTelNumber().equals("delete")) {
                System.out.println("2");
                ourUsers.setTelNumber(null);
            }

            // Если email или номер телефона были изменены, сохраняем обновленные данные
            if (ourUsers.getEmail() != null || ourUsers.getTelNumber() != null) {
                ourUserRepo.save(ourUsers);
                response.setStatusCode(200);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes changePassword(ReqRes info){
        ReqRes response = new ReqRes();
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            OurUsers ourUsers = ourUserRepo.findByUsername(username).orElseThrow();
            System.out.println(info.getPassword());
            if(info.getPassword() != null) {
                ourUsers.setPassword(passwordEncoder.encode(info.getPassword()));
                ourUserRepo.save(ourUsers);
                response.setStatusCode(200);
                response.setMessage("Change password");
                return response;
            }
            response.setStatusCode(200);
            response.setMessage("error");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;

    }


}


