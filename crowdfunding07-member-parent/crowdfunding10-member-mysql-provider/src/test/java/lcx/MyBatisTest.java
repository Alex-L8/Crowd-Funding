package lcx;

import lcx.entity.po.MemberPO;
import lcx.entity.vo.DetailProjectVO;
import lcx.entity.vo.PortalProjectVO;
import lcx.entity.vo.PortalTypeVO;
import lcx.mapper.MemberPOMapper;
import lcx.mapper.ProjectPOMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Create by LCX on 2/24/2022 10:46 PM
 */
@SpringBootTest
public class MyBatisTest {

    private Logger logger = LoggerFactory.getLogger(MyBatisTest.class);
    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Test
    public void test1() throws SQLException {
        System.out.println(dataSource.getClass());

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testMapper() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123123";
        String encode = passwordEncoder.encode(source);
        MemberPO memberPO = new MemberPO(null, "jack", encode, " 杰 克 ", "jack@qq.com", 1, 1, "杰克", "123123", 2);
        memberPOMapper.insert(memberPO);
    }

    @Test
    public void testProjectPOMapper(){
        List<PortalTypeVO> portalTypeVOS = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO portalTypeVO : portalTypeVOS) {
            String name = portalTypeVO.getName();
            String remark = portalTypeVO.getRemark();
            logger.info("name="+name+"remark="+remark);
            List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
            for (PortalProjectVO portalProjectVO : portalProjectVOList) {
                if (portalProjectVO==null){
                    continue;
                }
                logger.info(String.valueOf(portalProjectVO));
            }

        }
    }

    @Test
    public void testLoadDetailProjectVO(){

        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(18);
        /*List<String> detailPicturePathList = detailProjectVO.getDetailPicturePathList();
        for (String s : detailPicturePathList) {
        }*/
//        logger.info(String.valueOf(detailProjectVO));
        System.out.println(detailProjectVO);
    }
}
