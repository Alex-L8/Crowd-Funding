package lcx.service;

import com.github.pagehelper.PageInfo;
import lcx.entity.Role;

import java.util.List;

/**
 * Create by LCX on 2/8/2022 2:01 PM
 */
public interface RoleService {
    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void saveRole(Role role);

    void updateRole(Role role);

    void removeRole(List<Integer> roleIdList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);
}
