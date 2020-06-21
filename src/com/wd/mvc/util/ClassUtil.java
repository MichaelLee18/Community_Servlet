package com.wd.mvc.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class ClassUtil {
    public static List<String> getClassNameByPackage(String packagePath, boolean childPackage) {
        List<String> classNames = new ArrayList<>();
        packagePath = packagePath.replaceAll("\\.", "\\\\");
        try {
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL element = resources.nextElement();
                if (element == null)
                    continue;
                String type = element.getProtocol();
                if ("file".equals(type)) {
                    getClassNameByFilePath(element.getPath(),classNames, packagePath, childPackage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    private static void getClassNameByFilePath(String path,List<String> classNames, String packagePath, boolean childPackage) throws UnsupportedEncodingException {
        File file = new File(URLDecoder.decode(path, "UTF-8"));
        if (file.exists()) {
            if (file.isDirectory()) {
                if (childPackage) {
                    File[] files = file.listFiles();
                    for (File f : files) {
                        getClassNameByFilePath(f.getPath(),classNames, packagePath, true);
                    }
                }

            } else {
                String original = file.getPath();
                int start = original.indexOf(packagePath);
                int end = original.indexOf(".class");
                String className = original.substring(start, end).replaceAll("\\\\", "\\.");
                classNames.add(className);
            }
        }
    }

    public static void main(String[] args) {
        List<String> classNames = getClassNameByPackage("com", true);
        classNames.forEach(System.out::println);
    }
}
