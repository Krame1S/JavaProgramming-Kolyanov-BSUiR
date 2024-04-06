package com.lab1.isthesiteup.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.lab1.isthesiteup.entities.Server;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    Optional<Server> findByUrl(String url);

    @Query("SELECT s FROM Server s JOIN s.checks c WHERE c.status = :status")
    List<Server> findServersByCheckStatus(@Param("status") String status);
}
