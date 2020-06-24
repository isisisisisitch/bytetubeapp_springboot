package com.bytetube;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController//相当于之前ssm中的@controller+@responseBody
//http://localhost:8080/hello
public class Hello {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        //返回Hello SpringBoot!
        return "Hello SpringBoot!";
    }
}
