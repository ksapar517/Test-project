package com.example.test.controller;


import com.example.test.entity.BankShot;
import com.example.test.service.AuthService;
import com.example.test.service.BankShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
public class BankShotController {
    @Autowired
    private BankShotService bankShotService;

    @PostMapping("/moneyTransfer")
    public ResponseEntity<Object> moneyTransfer(@RequestBody BankShot bankShot) {
        System.out.println("salam");
        System.out.println(bankShot);
        return ResponseEntity.ok(bankShotService.moneyTransfer(bankShot));
    }
}
