package com.lab1.isthesiteup.repositories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.lab1.isthesiteup.entities.Server;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ServerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ServerRepository serverRepository;

    @Test
    public void testFindByUrl() {
        Server server = new Server();
        server.setUrl("http://example.com");
        entityManager.persist(server);
        entityManager.flush();

        Server found = serverRepository.findByUrl(server.getUrl()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getUrl()).isEqualTo(server.getUrl());
    }
}
