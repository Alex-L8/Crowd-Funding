package lcx.handler;

import lcx.api.MySQLRemoteService;
import lcx.constant.CrowdConstant;
import lcx.entity.vo.AddressVO;
import lcx.entity.vo.MemberLoginVO;
import lcx.entity.vo.OrderProjectVO;
import lcx.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Create by LCX on 3/11/2022 9:20 AM
 */
@Controller
public class OrderHandler {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session){
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(projectId,returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            OrderProjectVO orderProjectVO = resultEntity.getData();
            session.setAttribute(CrowdConstant.ORDER_PROJECT_VO,orderProjectVO);
        }
        return "confirm_return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String confirmShowOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session){

        // 1.把接收到的returnCount合并到session域中
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ORDER_PROJECT_VO);

        orderProjectVO.setReturnCount(returnCount);

        // 2.保证redis中的数据也跟着修改
        session.setAttribute(CrowdConstant.ORDER_PROJECT_VO,orderProjectVO);

        // 3.获取当前已登录的用户id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();
        // 4.查询目前的收货地址数据
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);

        // 5.
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> addressVOList = resultEntity.getData();
            session.setAttribute(CrowdConstant.ADDRESS_VO_LIST,addressVOList);
        }
        return "confirm_order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO,HttpSession session){
        // 1.执行地址信息的保存
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);

        // 2.从session域中获取OrderProjectVO对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ORDER_PROJECT_VO);

        // 3.从OrderProjectVO对象中获取returnCount
        Integer returnCount = orderProjectVO.getReturnCount();

        // 4. 重定向到指定地址，重新进入订单页面
        return "redirect:http://localhost/order/confirm/order/"+returnCount;
    }

}
