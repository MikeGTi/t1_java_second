package ru.t1.java.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import ru.t1.java.demo.controller.AccountController;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class T1JavaSecondApplicationTests {

    @Autowired
    private AccountController accountController;

    @Test
    void contextLoads() {
        assertThat(accountController).isNotNull();
    }
}
