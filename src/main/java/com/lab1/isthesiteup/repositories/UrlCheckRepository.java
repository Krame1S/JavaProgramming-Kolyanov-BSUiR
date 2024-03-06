package com.lab1.isthesiteup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lab1.isthesiteup.entities.UrlCheckEntity;

public interface UrlCheckRepository extends JpaRepository<UrlCheckEntity, Long> {
    
}
