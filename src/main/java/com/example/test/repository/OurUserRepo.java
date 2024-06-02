package com.example.test.repository;

import com.example.test.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OurUserRepo extends JpaRepository<OurUsers, Integer> {
    Optional<OurUsers> findByUsername(String username);
    Optional<OurUsers> findByEmail(String email);
    List<OurUsers> findByBirthdate(LocalDate birthdate);

    List<OurUsers> findByName(String name);
    Optional<OurUsers> findByTelNumber(String telNumber); // Corrected method name

    List<OurUsers> findByNameStartingWith(String name);
}
