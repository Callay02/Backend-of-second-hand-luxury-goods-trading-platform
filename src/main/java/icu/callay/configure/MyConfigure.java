package icu.callay.configure;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import icu.callay.interceptor.CorsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfigure implements WebMvcConfigurer {

    @Bean
    CorsInterceptor getCorsInterceptor() {
        return new CorsInterceptor();
    }

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //跨域
        registry.addInterceptor(getCorsInterceptor())
                .addPathPatterns("/**")
                .order(0);//使用order能设置拦截器执行顺序(序号越小优先级越高)

        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout")
                .excludePathPatterns("/user/hello")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/getcode")
                .order(1000);
    }
}
