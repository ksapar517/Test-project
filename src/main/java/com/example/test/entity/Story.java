package com.example.test.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "story")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "shot_id", nullable = false)
    private Integer shotId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
    private Double plusBalans;
    private Double minusBalans;
    private LocalDateTime processDate;
    private String title;
}
