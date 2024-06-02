package com.example.test.repository;

import com.example.test.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepo extends JpaRepository<Story, Integer> {
}
