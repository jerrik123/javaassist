package com.njq.nongfadai.bean;

/**
 * Copyright 2018/2/7 lcfarm All Rights Reserved
 * 请添加类/接口的说明：
 *
 * @Package: com.njq.nongfadai.bean
 * @author: Jerrik
 * @date: 2018/2/7 16:25
 */
public class JavassistClass {
    private String name="default";
    public JavassistClass(){
        name="me";
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void execute(){
        System.out.println(name);
        System.out.println("execute ok");
    }
}
