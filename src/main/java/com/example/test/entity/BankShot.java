package com.example.test.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "BankShot")
public class BankShot{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private Integer userId;
    @Column(nullable = false, unique = true)
    private String shotName;
    private Double balance;

}
