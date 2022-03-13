package lcx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lcx.entity.Role;
import lcx.entity.RoleExample;
import lcx.mapper.RoleMapper;
import lcx.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by LCX on 2/8/2022 2:01 PM
 */
@Service
public class RoleServiceImpl implements RoleService {
    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        // 1.开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        // 2.执行查询
        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);


        // 3.封装为PageInfo对象返回
        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> roleIdList) {
        RoleExample example=new RoleExample();

        RoleExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(roleIdList);

        roleMapper.deleteByExample(example);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
       return roleMapper.selectAssignedRole(adminId);

    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {

        return roleMapper.selectUnAssignedRole(adminId);
    }
}
