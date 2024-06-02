package com.example.test.service;

import com.example.test.controller.BankShotController;
import com.example.test.dto.ReqRes;
import com.example.test.entity.BankShot;
import com.example.test.entity.OurUsers;
import com.example.test.entity.Story;
import com.example.test.repository.BankShotRepo;
import com.example.test.repository.OurUserRepo;
import com.example.test.repository.StoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankShotService {

    private final BankShotRepo bankShotRepo;
    private final OurUserRepo ourUserRepo;
    private final StoryRepo storyRepo;
    public ReqRes addBalance(BankShot info) {
        ReqRes resp = new ReqRes();
        try {
            Story story = new Story();
            if(info.getBalance() <= 0) {
                resp.setMessage("Balance not  0 or balance < 0 ");
                return resp;
            }
            BankShot bankShot = bankShotRepo.findByShotName(info.getShotName()).orElseThrow();
            System.out.println("USER IS: " + bankShot);
            bankShot.setBalance(bankShot.getBalance() + info.getBalance());
            bankShotRepo.save(bankShot);
            story.setShotId(bankShot.getId());
            story.setUserId(bankShot.getUserId());
            story.setPlusBalans(info.getBalance());
            story.setTitle("Add Terminal");
            story.setProcessDate(LocalDateTime.now());  // Using LocalDateTime now
            storyRepo.save(story);

        } catch (Exception e) {
            System.out.println(e);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes moneyTransfer(BankShot info) {
        ReqRes resp = new ReqRes();
        try {
            if(info.getBalance() <= 0) {
                resp.setMessage("Balance not  0 or balance < 0 ");
                return resp;
            }
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            OurUsers ourUsers = ourUserRepo.findByUsername(userDetails.getUsername()).orElseThrow();
            BankShot minusBankShot = bankShotRepo.findByUserId(ourUsers.getId()).orElseThrow();
            BankShot plusBankShot = bankShotRepo.findByShotName(info.getShotName()).orElseThrow();

            if ((minusBankShot.getBalance() - info.getBalance()) >= 0) {
                minusBankShot.setBalance(minusBankShot.getBalance() - info.getBalance());
                plusBankShot.setBalance(plusBankShot.getBalance() + info.getBalance());
                bankShotRepo.save(minusBankShot);
                bankShotRepo.save(plusBankShot);
                Story plusStory = new Story();
                plusStory.setShotId(plusBankShot.getId());
                plusStory.setUserId(plusBankShot.getUserId());
                plusStory.setPlusBalans(info.getBalance());
                plusStory.setProcessDate(LocalDateTime.now());  // Using LocalDateTime now
                plusStory.setTitle("Transfer Money from " + minusBankShot.getShotName());
                storyRepo.save(plusStory);
                Story minusStory = new Story();
                minusStory.setShotId(minusBankShot.getId());
                minusStory.setUserId(minusBankShot.getUserId());
                minusStory.setMinusBalans(info.getBalance());
                minusStory.setProcessDate(LocalDateTime.now());  // Using LocalDateTime now
                minusStory.setTitle("Transfer Money to " + plusBankShot.getShotName());
                storyRepo.save(minusStory);
                resp.setMessage("Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Balance not enough");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            System.out.println(e);
            resp.setError(e.getMessage());
        }
        return resp;
    }
    @Scheduled(fixedDelay = 60000)
    public void scheduleFixedDelayTask() {
        List<BankShot> bankShots = bankShotRepo.findAll();
        for (BankShot bankShot : bankShots) {
            double currentBalance = bankShot.getBalance();
            double updatedBalance = currentBalance * 1.05; // Увеличение на 5%
            bankShot.setBalance(updatedBalance);
        }
        bankShotRepo.saveAll(bankShots);
    }

    public ReqRes createShot(OurUsers registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            BankShot bankShot = new BankShot();
            bankShot.setBalance((double) 5);
            bankShot.setShotName(registrationRequest.getShotName());
            bankShot.setUserId(registrationRequest.getId());
            bankShotRepo.save(bankShot);

            resp.setMessage("Successfully");
            resp.setStatusCode(200);
        }catch (Exception e){
            System.out.println(e);
            resp.setError(e.getMessage());
        }
        return resp;
    }

}
