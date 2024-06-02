package com.example.test.controller;


import com.example.test.dto.ReqRes;
import com.example.test.dto.UserBasicInfo;
import com.example.test.entity.BankShot;
import com.example.test.entity.OurUsers;
import com.example.test.service.ChangeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {
@Autowired
    private ChangeInfo changeInfoService;

    @PostMapping("/searchData")
    public ResponseEntity<List<UserBasicInfo>> searchData(@RequestBody ReqRes searchInfo) {
        ReqRes searchResult = changeInfoService.searchData(searchInfo);
        List<OurUsers> users = searchResult.getData();
        List<UserBasicInfo> userBasicInfos = new ArrayList<>();

        for (OurUsers user : users) {
            UserBasicInfo userInfo = new UserBasicInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setName(user.getName());
            userInfo.setShotName(user.getShotName());
            userBasicInfos.add(userInfo);

            // Printing user information
            System.out.println("Username: " + userInfo.getUsername() + ", Name: " + userInfo.getName() + ", Shot Name: " + userInfo.getShotName());
        }
        System.out.println(userBasicInfos);

        return ResponseEntity.ok(userBasicInfos);
    }

    @PostMapping("/changeinfo")
    public ResponseEntity<Object> changeinfo(@RequestBody ReqRes info) {
        return ResponseEntity.ok(changeInfoService.changeinfo(info));
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody ReqRes info) {
        return ResponseEntity.ok(changeInfoService.delete(info));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody ReqRes info) {
        return ResponseEntity.ok(changeInfoService.changePassword(info));
    }


}
