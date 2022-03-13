package lcx.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lcx.constant.AccessPassResources;
import lcx.constant.CrowdConstant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Create by LCX on 3/5/2022 10:51 AM
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {

        // 1.获取RequestContext对象
        RequestContext currentContext = RequestContext.getCurrentContext();

        // 底层上是通过 线程本地化(TreadLocal) 从当前线程上获取事先绑定的Request对象
        HttpServletRequest request = currentContext.getRequest();

        String servletPath = request.getServletPath();

        // 根据servletPath判断当前请求是否对应可以直接放行的特定功能
        boolean containResult = AccessPassResources.PASS_RES_SET.contains(servletPath);

        if (containResult) {
            // 当前请求为特定功能，故放行
            return false;
        }

        // 判断当前请求是否为静态资源

        // 若为静态资源则返回false，放行
        return !AccessPassResources.judgeCurrentServletPathWhetherContainStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {
        // 1.获取RequestContext对象
        RequestContext currentContext = RequestContext.getCurrentContext();

        // 底层上是通过 线程本地化(TreadLocal) 从当前线程上获取事先绑定的Request对象
        HttpServletRequest request = currentContext.getRequest();

        // 获取session对象
        HttpSession session = request.getSession();

        // 从session中获取已登录的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        // 判断loginMember是否为空
        if (loginMember == null) {
            HttpServletResponse response = currentContext.getResponse();

            // 6.将消息存入session中
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);

            // 7.重定向到auth-consumer工程中的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 可以忽略这个返回值
        return null;
    }

    @Override
    public String filterType() {
        // 这里返回"pre"意思是在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
