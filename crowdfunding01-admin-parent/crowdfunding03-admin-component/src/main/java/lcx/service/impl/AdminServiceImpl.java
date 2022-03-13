package lcx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lcx.constant.CrowdConstant;
import lcx.entity.Admin;
import lcx.entity.AdminExample;
import lcx.exception.LoginAcctDuplicateForAddException;
import lcx.exception.LoginAcctDuplicateForUpdateException;
import lcx.exception.LoginFailedException;
import lcx.mapper.AdminMapper;
import lcx.service.AdminService;
import lcx.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Create by LCX on 2/5/2022 6:35 PM
 */
@Service
public class AdminServiceImpl implements AdminService {
    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveAdmin(Admin admin) {
        // 1.密码加密
        String userPswd = admin.getUserPswd();
//         userPswd = CrowdUtil.md5(userPswd);
        userPswd = passwordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);

        // 2.生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        // 3.保存
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常全类名="+e.getClass().getName());
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctDuplicateForAddException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(null);
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1.根据登录账号查询Admin对象
        // ①创建Admin的Example对象
        AdminExample adminExample = new AdminExample();

        // ②创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();

        // ③在Criteria对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);

        // ④调用AdminMapper的方法执行查询
        List<Admin> list = adminMapper.selectByExample(adminExample);

        // 2.判断Admin对象是否为null
        if (list == null || list.size() == 0) {

            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = list.get(0);

        // 3.如果Admin对象为null则抛出异常
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4.如果Admin对象不为null则将数据库的密码从Admin对象中取出
        String userPswdDB = admin.getUserPswd();

        // 5.将表单中的明文密码进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);

        // 6.对密码进行比较
        if (!Objects.equals(userPswdDB, userPswdForm)) {
            // 7.如果密码不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 8.如果一致则返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1.调用PageHelper的静态方法开启分页功能
        // 这里充分体现了PageHelper的“非侵入式”设计：原本要做的查询不必有任何的修改
        PageHelper.startPage(pageNum, pageSize);

        // 2.执行插叙
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装到PageInfo对象中
        return new PageInfo<>(adminList);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void updateAdmin(Admin admin) {
        // 选择性的更新，对于null值的字段不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctDuplicateForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 为了简化操作：先根据adminId删除旧数据，再根据roleIdList保存新数据

        // 1.根据adminId删除旧数据
        adminMapper.deleteOldRelationship(adminId);

        // 2.根据roleIdList和adminId保存新的关联关系
        if(roleIdList != null && roleIdList.size() > 0){
            adminMapper.insertNewRelationship(adminId,roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {

        AdminExample adminExample = new AdminExample();

        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andLoginAcctEqualTo(username);

        List<Admin> adminList = adminMapper.selectByExample(adminExample);

        Admin admin = adminList.get(0);

        return admin;
    }
}
