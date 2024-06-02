package com.example.test.service;

import com.example.test.dto.ReqRes;
import com.example.test.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendSmsService {
   @Autowired
   private OurUserRepo ourUserRepo;

    public ReqRes sendSms(ReqRes signinRequest,String message1) {
        ReqRes response = new ReqRes();
        try {
            String to = signinRequest.getEmail(); // Replace with the recipient's SMS gateway address
            String from = "1904.01048@manas.edu.kg";
            String host = "smtp.gmail.com"; // For example, using Gmail's SMTP server
            final String username = "1904.01048@manas.edu.kg"; // Your email address
            final String password = "wsyp kozj mtkg gwmb"; // Your email password

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");

            // Get the Session object
            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));
                message.setSubject("Subject");
                message.setText(message1);
                Transport.send(message);
                System.out.println("Sent message successfully....");

            }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }
    public ReqRes sendSmsTelNumber(ReqRes signinRequest,String message1){
        ReqRes response = new ReqRes();
        try {
            response.setStatusCode(200);
            response.setError("send sms tel number failed");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }


}

