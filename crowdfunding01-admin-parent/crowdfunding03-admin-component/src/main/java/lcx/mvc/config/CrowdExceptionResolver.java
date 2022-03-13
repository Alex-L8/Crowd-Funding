package lcx.mvc.config;

import com.google.gson.Gson;
import lcx.constant.CrowdConstant;
import lcx.exception.LoginAcctDuplicateForAddException;
import lcx.exception.LoginAcctDuplicateForUpdateException;
import lcx.exception.LoginFailedException;
import lcx.util.CrowdUtil;
import lcx.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 针对不同异常进行处理
 * Create by LCX on 2/6/2022 9:59 AM
 */
@ControllerAdvice
public class CrowdExceptionResolver {

    // 权限不够异常
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(Exception exception,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName,exception,request,response);
    }

    // 处理修改账户时  用户名重复 的异常
    @ExceptionHandler(value = LoginAcctDuplicateForUpdateException.class)
    public ModelAndView resolveLoginAcctDuplicateForUpdateException(LoginAcctDuplicateForUpdateException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName="system-error";
        return commonResolve(viewName,exception, request, response);
    }

    // 处理新增账户时 用户名重复 的异常
    @ExceptionHandler(value = LoginAcctDuplicateForAddException.class)
    public ModelAndView resolveLoginAcctDuplicateForAddException(LoginAcctDuplicateForAddException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName="admin-add";
        return commonResolve(viewName,exception, request, response);
    }

    // 处理登录失败时的异常
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName="admin-login";
        return commonResolve(viewName,exception, request, response);
    }

    // 空指针异常
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName="system-error";
        return commonResolve(viewName,exception, request, response);
    }

    // 抽取通用代码
    private ModelAndView commonResolve(String viewName,Exception exception,HttpServletRequest request,HttpServletResponse response) throws IOException {
        // 判断请求类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        // 如果是ajax请求
        if(judgeResult){
            // 返回异常信息
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            Gson gson=new Gson();
            String json = gson.toJson(resultEntity);
            response.getWriter().write(json);

            // 由于上面已经通过原生的response对象返回了响应,所以不提供ModelAndView对象
            return null;
        }
        // 不是ajax请求
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
