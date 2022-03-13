package lcx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Create by LCX on 2/24/2022 9:23 PM
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaMainClass {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMainClass.class,args);
    }
}
