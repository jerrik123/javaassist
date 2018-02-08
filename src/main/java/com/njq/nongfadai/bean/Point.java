package com.njq.nongfadai.bean;

/**
 * Copyright 2018/2/8 lcfarm All Rights Reserved
 * 请添加类/接口的说明：
 *
 * @Package: com.njq.nongfadai.bean
 * @author: Jerrik
 * @date: 2018/2/8 13:46
 */
public class Point {
    int x, y;
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void throwException(){
        int i = 1/0;
    }
}
