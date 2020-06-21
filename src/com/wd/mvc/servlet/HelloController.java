package com.wd.mvc.servlet;

import com.wd.mvc.annotation.WDController;
import com.wd.mvc.annotation.WDRequestMapping;

@WDController
@WDRequestMapping("/app")
public class HelloController {
    @WDRequestMapping("/hello")
    public String hello(){
        System.out.println("....hello.........");
        return "hello";
    }

    @WDRequestMapping("/world")
    public String world(){
        System.out.println("....world.........");
        return "world";
    }

}
