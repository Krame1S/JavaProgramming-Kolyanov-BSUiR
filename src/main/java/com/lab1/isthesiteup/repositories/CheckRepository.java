package com.lab1.isthesiteup.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lab1.isthesiteup.entities.CheckEntity;

@Repository
public interface CheckRepository extends JpaRepository<CheckEntity, Long> {
    List<CheckEntity> findByServerEntityUrl(String url);
}