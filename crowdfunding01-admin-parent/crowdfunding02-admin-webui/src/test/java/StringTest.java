import lcx.mapper.AdminMapper;
import lcx.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Create by LCX on 2/6/2022 5:12 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml","classpath:spring-web-mvc.xml"})
public class StringTest {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testMd5(){
        String source = "123123";
        String encode = CrowdUtil.md5(source);
        System.out.println(encode);
    }

    @Test
    public void testBCryptPasswordEncoder(){
        String source = "123123";
        String encode = bCryptPasswordEncoder.encode("123123");
        System.out.println(encode);
    }
}
