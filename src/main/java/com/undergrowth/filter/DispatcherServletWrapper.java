package com.undergrowth.filter;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;

/**
 * DispatcherServletWrapper
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-08-31-18:29
 */
@Component
public class DispatcherServletWrapper extends DispatcherServlet {

    @Override
    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        HandlerExecutionChain chain = super.getHandler(request);
        Object handler = chain.getHandler();
        if (!(handler instanceof HandlerMethod)) {
            return chain;
        }

        HandlerMethod hm = (HandlerMethod) handler;
        if (!hm.getBeanType().isAnnotationPresent(Controller.class)) {
            return chain;
        }

        //本扩展仅处理@Controller注解的Bean
        return new HandlerExecutionChainWrapper(chain, request, getWebApplicationContext());
    }

}

