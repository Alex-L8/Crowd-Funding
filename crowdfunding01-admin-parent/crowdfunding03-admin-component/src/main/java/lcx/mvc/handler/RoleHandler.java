package lcx.mvc.handler;

import com.github.pagehelper.PageInfo;
import lcx.entity.Role;
import lcx.service.RoleService;
import lcx.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create by LCX on 2/8/2022 2:03 PM
 */
@RestController
public class RoleHandler {

    @Autowired
    private RoleService roleService;

//    @ResponseBody
    @PreAuthorize(("hasRole('部长')"))
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword",defaultValue = "") String keyword
        ){

        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);


        return ResultEntity.successWithData(pageInfo);

    }

    @RequestMapping("/role/save.json")
    public ResultEntity<String> addRole(Role role){
        roleService.saveRole(role);

        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){
        roleService.updateRole(role);

        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeRole(@RequestBody List<Integer> roleIdList){

        roleService.removeRole(roleIdList);

        return ResultEntity.successWithoutData();
    }

}
