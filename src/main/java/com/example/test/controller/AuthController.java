package com.example.test.controller;

import com.example.test.dto.ReqRes;
import com.example.test.entity.BankShot;
import com.example.test.entity.OurUsers;
import com.example.test.repository.OurUserRepo;
import com.example.test.service.AuthService;
import com.example.test.service.BankShotService;
import com.example.test.service.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JWTUtils jwtUtils;
    private final AuthService authService;
    private final BankShotService bankShotService;
    private final OurUserRepo ourUserRepo;

    @PostMapping("/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes signUpRequest){
        return ResponseEntity.ok(authService.register(signUpRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes signInRequest){
        return ResponseEntity.ok(authService.login(signInRequest));
    }
    @PostMapping("/addBalace")
    public ResponseEntity<Object> addBalace(@RequestBody BankShot bankShot) {
        return ResponseEntity.ok(bankShotService.addBalance(bankShot));
    }

    private final String SECRET_KEY = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3"; // This should be stored securely
    @GetMapping("/confirm-account/{token}")
    public ResponseEntity<Boolean> confirmAccount(@PathVariable String token) {
        try {

            String tokenUsername = decodeToken(token);
            System.out.println(tokenUsername);
            OurUsers ourUsers = ourUserRepo.findByUsername(tokenUsername).orElseThrow();
            System.out.println(ourUsers);
            ourUsers.setEnabled(true);
            ourUserRepo.save(ourUsers);
            bankShotService.createShot(ourUsers);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(false);
        }
    }

    private String decodeToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Assuming the subject contains the username
    }
}
