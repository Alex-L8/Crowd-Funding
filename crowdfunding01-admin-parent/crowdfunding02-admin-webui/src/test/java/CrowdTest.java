import lcx.entity.Admin;
import lcx.entity.Role;
import lcx.mapper.AdminMapper;
import lcx.mapper.RoleMapper;
import lcx.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Create by LCX on 2/5/2022 3:32 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testRoleSave(){
        for (int i = 0; i < 200; i++) {
            roleMapper.insert(new Role(null,"role"+i));
        }
    }

    @Test
    public void insertAdmin(){
        for (int i = 0; i < 200; i++) {
            Admin admin = new Admin(null, "tom"+i, "123456"+i, "汤姆"+i, "tom@qq.com"+i, null);
            adminMapper.insert(admin);
        }
    }
    @Test
    public void testInsertAdmin(){
        Admin admin = new Admin(null, "tom", "123456", "汤姆", "tom@qq.com", null);
        int i = adminMapper.insert(admin);
        System.out.println(i);
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testTx(){
        Admin admin = new Admin(null, "jerry", "666666", "杰瑞", "jerry@qq.com", null);
        adminService.saveAdmin(admin);

    }
}
