package lcx;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Create by LCX on 2/24/2022 10:46 PM
 */
@SpringBootTest
public class MyRedisTest {

    private Logger logger = LoggerFactory.getLogger(MyRedisTest.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test1(){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("apple","red");
    }

}
