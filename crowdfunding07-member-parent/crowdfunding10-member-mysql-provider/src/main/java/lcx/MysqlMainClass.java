package lcx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Create by LCX on 2/24/2022 9:23 PM
 */

@MapperScan("lcx.mapper")
@SpringBootApplication
public class MysqlMainClass {
    public static void main(String[] args) {
        SpringApplication.run(MysqlMainClass.class,args);
    }
}
