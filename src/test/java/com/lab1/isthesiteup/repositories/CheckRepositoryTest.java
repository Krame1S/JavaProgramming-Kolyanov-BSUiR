// package com.lab1.isthesiteup.repositories;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
// import com.lab1.isthesiteup.entities.Check;
// import static org.assertj.core.api.Assertions.assertThat;
// import java.util.List;

// @DataJpaTest
// class CheckRepositoryTest {

//     @Autowired
//     private TestEntityManager entityManager;

//     @Autowired
//     private CheckRepository checkRepository;

//     @Test
//     void testFindByServerUrl() {
//         Check check = new Check();
//         check.setUrl("http://example.com");
//         entityManager.persist(check);
//         entityManager.flush();

//         List<Check> found = checkRepository.findByServerUrl(check.getUrl());

//         assertThat(found).isNotEmpty();
//         assertThat(found.get(0).getUrl()).isEqualTo(check.getUrl());
//     }
// }
