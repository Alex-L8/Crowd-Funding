package lcx.mvc.handler;

import lcx.entity.Admin;
import lcx.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Create by LCX on 2/5/2022 9:26 PM
 */
//@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private final Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @RequestMapping("/test/ssm.html")
    public String testSsm(Model model,HttpServletRequest request){

        List<Admin> adminList=adminService.getAll();
        model.addAttribute("adminList", adminList);

        /*String a=null;
        System.out.println(a.length());*/
        System.out.println(10/0);
        return "target";
    }

    @ResponseBody
    @RequestMapping("/send/array1.html")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array){

        for (Integer number : array){
            System.out.println(number);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array2.html")
    public String testReceiveArrayTwo(@RequestBody List<Integer> array){

        for (Integer number : array){
            logger.info("number="+number); //注意是 org.slf4j.Logger，不是jul中的Logger
        }
        return "success";
    }

}
