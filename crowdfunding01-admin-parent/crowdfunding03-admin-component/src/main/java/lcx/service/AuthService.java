package lcx.service;

import lcx.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * Create by LCX on 2/10/2022 10:56 PM
 */
public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
