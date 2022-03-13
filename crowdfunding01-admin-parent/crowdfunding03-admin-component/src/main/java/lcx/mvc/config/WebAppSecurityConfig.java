package lcx.mvc.config;

import lcx.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Create by LCX on 2/11/2022 9:02 PM
 */
@Configuration            // 设置为配置类
@EnableWebSecurity        // 开启web环境下的权限控制功能
// 启用全局方法权限控制功能， 并且设置 prePostEnabled = true。 保证@PreAuthority、@PostAuthority、 @PreFilter、 @PostFilter 生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 需要继承WebSecurityConfigurerAdapter
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 在这里声明，无法在XxxService中装配
    /*@Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }*/

    /* @Bean
     @Override
     public AuthenticationManager authenticationManagerBean() throws Exception {
         return super.authenticationManagerBean();
     }*/
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        // 临时使用内存版登录的模式测试代码
        // 5.0以上的版本会报错，目前还不知道问题在哪 降版本才能使用
        /*builder.inMemoryAuthentication()
                .withUser("tom").password("123123").roles("ADMIN");*/

        // 正式功能中使用基于数据库的认证
        builder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("tom").password("123123").roles("ADMIN");

    }*/

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        // String数组，列出需要放行的资源的路径
        String[] permitUrls = {"/admin/to/login/page.html","/index.jsp","/bootstrap/**",
                "/crowd/**","/css/**","/fonts/**","/img/**",
                "/jquery/**","/layer/**","/script/**","/ztree/**"};
        security.authorizeRequests()    // 对请求进行授权
                .antMatchers(permitUrls).permitAll()
                .antMatchers("/admin/get/page.html")
                .access("hasRole('经理') or hasAuthority('user:get')")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, e) -> {
                    request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                    request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request, response);
                })
                .and()
                .csrf().disable()         // 禁用防跨站请求伪造
                .formLogin()                // 开启表单登录的功能
                .loginPage("/admin/to/login/page.html")     // 指定登录页面
                .loginProcessingUrl("/security/do/login.html")  // 指定处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html")  // 指定登录成功后前往的地址
                .usernameParameter("loginAcct").passwordParameter("userPswd")
                .and()
                .logout().logoutUrl("/security/do/logout.html").logoutSuccessUrl("/admin/to/login/page.html");
    }
}
