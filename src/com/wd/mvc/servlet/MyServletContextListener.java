package com.wd.mvc.servlet;

import com.wd.mvc.util.ClassUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.stream.Collectors;
@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        init( servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void init(ServletContext servletContext){
        List<String> classNames = ClassUtil.getClassNameByPackage("com",true);
        List<? extends Class<?>> classes = classNames.stream().map(name -> {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        WDDispatcherServlet dispatcherServlet = new WDDispatcherServlet(classes);
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");

    }
}
