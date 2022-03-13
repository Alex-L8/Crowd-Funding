package lcx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Create by LCX on 3/11/2022 8:14 PM
 */
@EnableFeignClients
@SpringBootApplication
public class PayMainClass {
    public static void main(String[] args) {
        SpringApplication.run(PayMainClass.class,args);
    }
}
