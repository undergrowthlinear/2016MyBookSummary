package com.undergrowth.aspectj;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-11:57
 */
@Configuration
public class AspectConguration {

    @Bean
    public StatsServiceInterceptor statsServiceInterceptor() {
        return new StatsServiceInterceptor();
    }

}