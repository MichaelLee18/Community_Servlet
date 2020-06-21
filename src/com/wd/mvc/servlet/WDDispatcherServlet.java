package com.wd.mvc.servlet;

import com.wd.mvc.annotation.WDController;
import com.wd.mvc.annotation.WDRequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WDDispatcherServlet extends HttpServlet {
    private List<? extends Class<?>> classes;
    private Map<String,WDMappingHandler> handlerMaps = new ConcurrentHashMap<>();
    public WDDispatcherServlet(List<? extends Class<?>> classes) {
        this.classes = classes;
        initHandlerMappings();

    }

    private void initHandlerMappings() {
        classes.stream().forEach(clazz->{
            boolean present = clazz.isAnnotationPresent(WDController.class);
            if (present){
                Method[] methods = clazz.getMethods();
                Arrays.asList(methods).stream().forEach(method -> {
                    if(method.isAnnotationPresent(WDRequestMapping.class)){
                        WDRequestMapping annotation = method.getAnnotation(WDRequestMapping.class);
                        String url = annotation.value();
                        WDMappingHandler wdMappingHandler = new WDMappingHandler(url,method,clazz);
                        handlerMaps.put(url,wdMappingHandler);
                    }
                });
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatcher(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //uri:/Commubity_Servlet_war_exploded/hello
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String url = uri.substring(contextPath.length());
        WDMappingHandler handler = handlerMaps.get(url);
        handler.invoke();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
