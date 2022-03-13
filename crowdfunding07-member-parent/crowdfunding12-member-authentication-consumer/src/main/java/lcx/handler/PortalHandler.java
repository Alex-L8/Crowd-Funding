package lcx.handler;

import lcx.api.MySQLRemoteService;
import lcx.constant.CrowdConstant;
import lcx.entity.vo.PortalTypeVO;
import lcx.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Create by LCX on 2/28/2022 9:03 PM
 */
@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(Model model){
        // 省略从数据库加载数据的操作
        // 1.调用MySQLRemoteService提供的方法查询首页需要显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();

        // 2.检查查询结果
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            // 3.获取查询结果的数据
            List<PortalTypeVO> list = resultEntity.getData();

            // 4.存入模型，放到页面中显示
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,list);

        }
        return "portal";
    }
}



