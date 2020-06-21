package com.wd.mvc.servlet;

import java.lang.reflect.Method;

public class WDMappingHandler {
    private String url;
    private Method method;
    private Class target;
    private Object[] params;
    public WDMappingHandler() {
    }

    public WDMappingHandler(String url, Method method,Class target) {
        this.url = url;
        this.method = method;
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void invoke() throws Exception {
        method.invoke(target.getDeclaredConstructor().newInstance(),null);
    }
}
