package com.njq.nongfadai.client;

import com.njq.nongfadai.bean.Point;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright 2018/2/8 lcfarm All Rights Reserved
 * 请添加类/接口的说明：
 *
 * @Package: com.njq.nongfadai.client
 * @author: Jerrik
 * @date: 2018/2/8 12:50
 */
public class Samples {

    @Test
    public void testFirst() throws Exception {
        ClassPool cPool = ClassPool.getDefault();//hashtable key:完整类名 value:CtClass
        CtClass ctClass = cPool.get("com.njq.nongfadai.bean.Rectangle");
        ctClass.writeFile("dd");
        //ctClass.toBytecode();
        //ctClass.toClass();
    }

    @Test
    public void testMakeClass() throws Exception {
        ClassPool cPool = ClassPool.getDefault();
        CtClass ctClass = cPool.makeClass("Hello");
        ctClass.writeFile("dd");
    }

    @Test
    public void testMakeInterface() throws Exception {
        ClassPool cPool = ClassPool.getDefault();
        CtClass ctClass = cPool.makeInterface("IGenerator");
        ctClass.writeFile("dd");
    }

    @Test
    public void insertClassPath() throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath("/usr/local/javalib");//指定classpath
    }


    //在方法之前插入  $1 就是第一个参数  $2 第二个参数
    @Test
    public void test$$() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.njq.nongfadai.bean.Point");
        CtMethod m = cc.getDeclaredMethod("move");
        m.insertBefore("{ " +
                "System.out.println($1); \n" +
                "System.out.println($2); " +
                "}");
        cc.writeFile("dd");

        Point point = (Point) cc.toClass().newInstance();
        point.move(3, 4);
    }

    @Test
    public void testAddCatch() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.njq.nongfadai.bean.Point");
        CtMethod ctMethod = ctClass.getDeclaredMethod("throwException");

        CtClass etype = ClassPool.getDefault().get("java.lang.ArithmeticException");
        ctMethod.addCatch("{ System.out.println(\"-->\" + $e); throw $e;}", etype);

        ctClass.writeFile("dd");
        Point point = (Point) ctClass.toClass().newInstance();
        point.throwException();

        /**
         * ouptut:
         *      try {
                     int i = 1 / 0;
                     return;
                } catch (ArithmeticException localArithmeticException) {
                     System.out.println("-->" + localArithmeticException);
                    throw localArithmeticException;
                }
         */
    }

    @Test
    public void testMakeNewMethod() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("com.njq.nongfadai.bean.Proxy$0");
        CtMethod method1 = CtNewMethod.make("public abstract int doHandle(int indexId);", ctClass);
        CtMethod method2 = CtNewMethod.make("public abstract int notice(int indexId);", ctClass);

        //必须先添加Method,再设置body
        ctClass.addMethod(method1);
        ctClass.addMethod(method2);

        method1.setBody("{ System.out.println($1);return 1;}");
        method2.setBody("{ System.out.println($1);return 2;}");

        ctClass.setModifiers(ctClass.getModifiers() & ~Modifier.ABSTRACT);

        ctClass.writeFile("dd");
    }

    @Test
    public void testAddField()  throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.njq.nongfadai.bean.Point");
        //建议直接用字符串拼接的方式
        //替代方式: CtField f = new CtField(CtClass.intType, "z", point);
        CtField f = CtField.make("public static final int COUNT = 0;", ctClass);
        ctClass.addField(f);
        ctClass.writeFile("dd");
    }

    //构造函数 初始化
    @Test
    public void testInititalInConstructor() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.njq.nongfadai.bean.Point");

        CtConstructor ctConstructor = ctClass.getConstructors()[0];
        ctConstructor.setBody("{System.out.println(\"construct init...\");}");
        ctClass.writeFile("dd");
    }

    /**
     * 注意:针对tomcat其他服务器,ClassLoader都是自己独有的,所以需要自定义传入classLoader来加载class
     */
    private static final Map<ClassLoader, ClassPool> POOL_MAP = new ConcurrentHashMap<ClassLoader, ClassPool>();

    /**
     * dubbox部分代码:
     *  public static ClassPool getClassPool(ClassLoader loader)
     {
         if( loader == null )
         return ClassPool.getDefault();

         ClassPool pool = POOL_MAP.get(loader);
         if( pool == null )
         {
         pool = new ClassPool(true);
         pool.appendClassPath(new LoaderClassPath(loader));
         POOL_MAP.put(loader, pool);
         }
         return pool;
     }
     */

}
