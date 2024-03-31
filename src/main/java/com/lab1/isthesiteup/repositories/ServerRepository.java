package com.lab1.isthesiteup.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.lab1.isthesiteup.entities.ServerEntity;

@Repository
public interface ServerRepository extends JpaRepository<ServerEntity, Long> {
    ServerEntity findByUrl(String url);

     @Query("SELECT s FROM ServerEntity s JOIN s.checks c WHERE c.status = :status")
    List<ServerEntity> findServersByCheckStatus(@Param("status") String status);
}
