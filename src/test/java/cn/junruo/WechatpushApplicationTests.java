package cn.junruo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SpringBootTest
@Configuration

class WechatpushApplicationTests {

    @Value("${config.openID}")
    private String[] arr;

    @Test
    void contextLoads() {
        for (String s : arr) {
            System.out.println(s);
        }
    }

}
