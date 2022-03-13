package lcx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Create by LCX on 3/1/2022 8:40 PM
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/auth/member/to/reg/page").setViewName("member-reg");

        registry.addViewController("/auth/member/to/login/page").setViewName("member-login");

        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");

        registry.addViewController("/member/my/crowd").setViewName("member-crowd");



    }
}
