package com.undergrowth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description springboot欢迎你
 * @date 2017-01-06-23:14
 */
@RequestMapping(value = "/helloWorld")
@Controller
public class SpringBootHelloWorld {

  @RequestMapping(value = "/hello")
  @ResponseBody
  public String helloWorld(@RequestParam String name) {
    return SpringBootHelloWorld.class.getSimpleName() + "-" + System.currentTimeMillis() + "-欢迎"
        + name;
  }

}
