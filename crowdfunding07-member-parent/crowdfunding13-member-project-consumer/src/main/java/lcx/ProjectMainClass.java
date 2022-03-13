package lcx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Create by LCX on 3/6/2022 10:59 AM
 */
@EnableFeignClients
@SpringBootApplication
public class ProjectMainClass {
    public static void main(String[] args) {
        SpringApplication.run(ProjectMainClass.class,args);
    }
}
