package lcx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Create by LCX on 2/28/2022 8:40 PM
 */
@EnableFeignClients
@SpringBootApplication
public class AuthenticationMainClass {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationMainClass.class,args);
    }
}
