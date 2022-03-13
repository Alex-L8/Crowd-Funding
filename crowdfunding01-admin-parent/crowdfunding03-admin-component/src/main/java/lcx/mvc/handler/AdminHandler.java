package lcx.mvc.handler;

import com.github.pagehelper.PageInfo;
import lcx.constant.CrowdConstant;
import lcx.entity.Admin;
import lcx.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Create by LCX on 2/6/2022 3:46 PM
 */
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    /**
     * 校验登录
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPswd") String userPswd, HttpSession session){

        // 调用Service方法执行登录检查
        Admin admin=adminService.getAdminByLoginAcct(loginAcct,userPswd);

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        // 为了避免跳转到后台页面再刷新浏览器导致重复提交登录表单，重定向到目标页面
        return "redirect:/admin/to/main/page.html";
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        // 强制session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 分页查询
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
//    @PreAuthorize("hasRole('经理')")
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            Model model
            ){
        // 调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        model.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);

        return "admin-page";
    }

    /**
     * 删除某个账户
     * @param adminId
     * @return
     */
    @RequestMapping("admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword
    ){
        // 执行删除
        adminService.remove(adminId);

        // 页面跳转：回到分页页面

        // 方案一：直接转发到admin-page.jsp 因为没有携带分页功能需要的参数 会无法显示分页数据
        //return "admin-page";

        // 方案二： 转发到/admin/get/page.html请求地址，一旦刷新页面会重复执行删除，浪费性能
        // return "forward:/admin/get/page.html";

        // 方案三： 重定向到/admin/get/page.html
        // 同时为了保持原本所在的页面和查询关键词再附加pageNum和keyword两个请求参数
        // 目前存在的问题：1.删除最后一页的最后一条数据后，重定向后不能自动显示倒数第二页的数据
        //               2.没删除一条数据就要查询一次，数据量大的时候可以将查询的数据放入请求域中，避免多次查询
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /**
     *  新增Admin账户
     * @param admin
     * @return
     */
    @PreAuthorize("hasAuthority('user:add')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);

        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            Model model
            ){
        Admin admin=adminService.getAdminById(adminId);
        model.addAttribute("admin", admin);
        return "admin-edit";
    }

    @RequestMapping("/admin/update.html")
    public String update(Admin admin,@RequestParam("pageNum") Integer pageNum,@RequestParam("keyword") String keyword){
        adminService.updateAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
}
