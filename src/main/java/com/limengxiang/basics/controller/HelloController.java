package com.limengxiang.basics.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * RestController注解告知框架这是一个提供Restful API的组件
 */
@RestController
public class HelloController {

    /**
     * 框架自动获取请求参数传给方法
     * name参数必传
     * RequestMapping是路由映射注解，可以指定请求路径和请求方法
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(@RequestParam("name") String name) {
        return "Hello, " + name;
    }

    /**
     * 手动从request对象中获取参数
     * 框架自动解决依赖，注入request对象
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/params", method = RequestMethod.POST)
    public Map params(HttpServletRequest request) {
        // 获取一个参数
        String name = request.getParameter("name");
        System.out.println("param name:" + name);
        // 获取所有参数
        Map paramMap = request.getParameterMap();
        System.out.println("param map:" + paramMap);

        return paramMap;
    }
}
