package com.aec.aec.AuthSrv;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import com.aec.aec.AuthSrv.AuthServiceApplication;;

@SpringBootTest(classes = AuthServiceApplication.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "jwt.secret=Cuetm0BgR8Bjk8Mo2OXtOlOxkc/N1K2g3vKx82o8Lcw="
})
class AuthServiceApplicationTests {

    @Test
    void contextLoads() { }
}

