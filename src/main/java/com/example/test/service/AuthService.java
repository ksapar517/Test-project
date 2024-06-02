package com.example.test.service;
import com.example.test.dto.ReqRes;
import com.example.test.entity.BankShot;
import com.example.test.entity.OurUsers;
import com.example.test.repository.BankShotRepo;
import com.example.test.repository.OurUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OurUserRepo ourUserRepo;
    private final BankShotRepo bankShotRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SendSmsService sendSmsService;
    @Value("${account.confirm.base-url}")
    private String baseUrl;
    @Value("${server.port}")
    private String port;

    public ReqRes register(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            OurUsers ourUsers = new OurUsers();
            if((registrationRequest.getEmail() != null || registrationRequest.getTelNumber() != null) && registrationRequest.getPassword().length() >= 6 && registrationRequest.getPassword().length() <= 8) {
                ourUsers.setUsername(registrationRequest.getUsername());
                ourUsers.setEmail(registrationRequest.getEmail());
                ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                ourUsers.setName(registrationRequest.getName());
                ourUsers.setSurName(registrationRequest.getSurName());
                ourUsers.setBirthdate(registrationRequest.getBirthdate());
                ourUsers.setTelNumber(registrationRequest.getTelNumber());
                ourUsers.setEnabled(false);
                String token = jwtUtils.generateToken(ourUsers);
                ourUsers.setShotName(String.valueOf(0));
                String link = baseUrl + ":" + port + "/auth/confirm-account/" + token;

                if(registrationRequest.getEmail() != null) {
                    sendSmsService.sendSms(registrationRequest, link);
                }else{
                    sendSmsService.sendSmsTelNumber(registrationRequest, link);
                }
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                ourUsers.setShotName(String.valueOf(ourUsers.getId()+100000));
                 ourUserRepo.save(ourUsers);

                if (ourUserResult != null) {
                    resp.setMessage("Sen link to "+ourUsers.getEmail());
                    resp.setStatusCode(200);
                }
            }
            else {
                resp.setMessage("error");
            }

        }catch (Exception e){
            System.out.println(e);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes login(ReqRes signinRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
            var user = ourUserRepo.findByUsername(signinRequest.getUsername()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            response.setMessage("Successfully");
            response.setToken(jwt);
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }


}
