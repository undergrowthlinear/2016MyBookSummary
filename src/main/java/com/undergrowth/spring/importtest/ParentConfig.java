package com.undergrowth.spring.importtest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description ParentConfig
 * @date 2017-09-23-11:45
 */
@Configuration
@Import({JavaConfigA.class, JavaConfigB.class})
public class ParentConfig {
    //Any other bean definitions
}
