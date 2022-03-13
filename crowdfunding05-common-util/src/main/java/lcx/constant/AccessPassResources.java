package lcx.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * Create by LCX on 3/5/2022 10:00 AM
 */
public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<>();

    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/message.json");
//        PASS_RES_SET.add("/");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    /**
     * 判断某个servletPath的值是否对应某个静态资源
     * @param servletPath
     * @return
     */
    public static boolean judgeCurrentServletPathWhetherContainStaticResource (String servletPath) {

        // 1.排除字符串无效的情况
        if (servletPath == null || servletPath.length() == 0 || servletPath == "/") {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        // 根据 "/" 拆分ServletPath字符串
        String[] split = servletPath.split("/");

        // /aa/bb/cc => ["","aa","bb","cc"]
        // 3.考虑到第一个斜杠经过拆分后得到一个空字符串,所以取数组中的第二个元素
        String firstLeverPath = split[1];
        return STATIC_RES_SET.contains(firstLeverPath);
    }

    /*public static void main(String[] args) {
        boolean b = judgeCurrentServletPathWhetherContainStaticResource("/");
        System.out.println(b);
    }*/
}
