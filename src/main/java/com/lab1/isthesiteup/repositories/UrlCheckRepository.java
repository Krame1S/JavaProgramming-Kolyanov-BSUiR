package com.lab1.isthesiteup.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lab1.isthesiteup.entities.UrlCheckEntity;

public interface UrlCheckRepository extends JpaRepository<UrlCheckEntity, Long> {
    List<UrlCheckEntity> findByTimeBetween(LocalDateTime start, LocalDateTime end);
}
