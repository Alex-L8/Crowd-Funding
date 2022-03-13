package lcx.handler;

import lcx.api.MySQLRemoteService;
import lcx.api.RedisRemoteService;
import lcx.config.ShortMessageProperties;
import lcx.constant.CrowdConstant;
import lcx.entity.po.MemberPO;
import lcx.entity.vo.MemberLoginVO;
import lcx.entity.vo.MemberVO;
import lcx.util.CrowdUtil;
import lcx.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Create by LCX on 3/1/2022 11:22 PM
 */
@Controller
public class MemberHandler {
    @Autowired
    private ShortMessageProperties properties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    /*@Autowired
    private BCryptPasswordEncoder passwordEncoder;*/

    /**
     * 执行退出登录请求
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:http://localhost/";
    }


    /**
     * 执行登录请求
     * @param loginacct
     * @param userpswd
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/do/login")
    public String login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd, Model model, HttpSession session){
        // 1.调用远程接口根据账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getData();
        if (memberPO == null) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 2.比较数据库中的密码和表单中的密码是否相同
        String userpswdDataBase = memberPO.getUserpswd();// 加密过的
        // 将表单中的密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // matches注意参数顺序，第一个填没加密的表单密码，第二个填数据库中加密过的密码
        boolean matchResult = passwordEncoder.matches(userpswd, userpswdDataBase);
        if(!matchResult) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 3.创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
        return "redirect:http://localhost/auth/member/to/center/page";
    }

    /**
     * 执行注册请求
     * @param memberVO
     * @param model
     * @return
     */
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, Model model){

        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();
        // 2.拼Redis中存储验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        // 3.从Redis中读取key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);
        // 4.检查查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if (redisCode == null) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        // 5.如果从Redis中查询到value就比较表单验证码和Redis验证码
        String formCode = memberVO.getCode();
        if (!Objects.equals(formCode,redisCode)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        // 6.如果验证码一致，则从Redis中删除key
//        redisRemoteService.removeRedisKeyRemote(key);
        // 7.执行密码的加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswdBeforeEncode = memberVO.getUserpswd();
        String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);
        memberVO.setUserpswd(userpswdAfterEncode);
        // 8.执行保存
        // ①.创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();

        // ②.借用BeanUtils复制属性
        BeanUtils.copyProperties(memberVO,memberPO);

        // ③.调用远程的方法
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberResultEntity.getMessage());
            return "member-reg";
        }
        // 使用重定向避免刷新浏览器导致重新执行注册的流程
        return "redirect:http://localhost/auth/member/to/login/page";
    }

    /**
     *  执行发送短信请求
     * @param phoneNum
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum){
        // 1.发送验证码到phoneNum手机上
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendCodeByShortMessage(
                properties.getHost(),
                properties.getPath(),
                properties.getMethod(),
                properties.getAppCode(), phoneNum,
                properties.getTemplate_id());

        // 2.判断短信发送结果
        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            // 3.发送成功，则将验证码存入Redis
            String code = sendMessageResultEntity.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            long time = 20;
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, time, TimeUnit.MINUTES);

            /*if(ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){
                return ResultEntity.successWithoutData();
            }else {
                return saveCodeResultEntity;
            }*/
            return saveCodeResultEntity;
        }else {
            return sendMessageResultEntity;
        }

    }
}
