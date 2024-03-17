package com.lab1.isthesiteup.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lab1.isthesiteup.entities.ServerEntity;

@Repository
public interface ServerRepository extends JpaRepository<ServerEntity, Long> {
    Optional<ServerEntity> findByUrl(String url);
}
