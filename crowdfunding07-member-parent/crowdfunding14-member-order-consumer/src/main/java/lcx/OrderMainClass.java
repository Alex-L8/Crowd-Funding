package lcx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Create by LCX on 3/10/2022 7:21 PM
 */
@EnableFeignClients
@SpringBootApplication
public class OrderMainClass {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainClass.class,args);
    }
}
