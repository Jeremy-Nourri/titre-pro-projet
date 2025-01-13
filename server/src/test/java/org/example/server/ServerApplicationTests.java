package org.example.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ServerApplicationTests {

    @BeforeAll
    static void setupEnvironment() {
        Dotenv dotenv = Dotenv
                .configure()
                .directory("server")
                .ignoreIfMissing()
                .load();


        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET", "default-secret"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION", "3600"));
    }

    @Test
    void contextLoads() {
    }

}
