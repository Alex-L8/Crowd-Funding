package lcx.service;

import com.github.pagehelper.PageInfo;
import lcx.entity.Admin;

import java.util.List;

/**
 * Create by LCX on 2/5/2022 6:35 PM
 */
public interface AdminService {
    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void remove(Integer adminId);

    Admin getAdminById(Integer adminId);

    void updateAdmin(Admin admin);

    void saveAdminRoleRoleRelationship(Integer adminId, List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String username);
}
