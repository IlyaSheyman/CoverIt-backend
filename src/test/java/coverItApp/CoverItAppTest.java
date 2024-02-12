package coverItApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class CoverItAppTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
    }
}
