package com.example.test.repository;

import com.example.test.entity.BankShot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankShotRepo extends JpaRepository<BankShot, Integer> {
    Optional<BankShot> findByShotName(String shotName);
    Optional<BankShot> findByUserId(Integer userId);
}