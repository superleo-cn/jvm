package com.poplar.classload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

public class ClassLoadTest16 extends ClassLoader {

    private String classLoaderName;

    public ClassLoadTest16() {

    }

    public ClassLoadTest16(String classLoaderName) {
        super();
        this.classLoaderName = classLoaderName;
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = this.definedClass(name);
        return super.defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] definedClass(String name) {
        byte[] bytes = null;
        String path = "/Users/kylin/Documents/dev/java/projects/workspace/scala-leetcode/target/classes/";
        ///Users/kylin/Documents/dev/java/projects/workspace/scala-leetcode/target/classes/com/leecode/array/Solution_1.class
        try (InputStream is = new FileInputStream(new File(path + name.replace(".", "/") + ".class"));
//        try (InputStream is = new FileInputStream(Objects.requireNonNull(this.getResource(name.replace(".", "/") + ".class")).getFile());
             ByteArrayOutputStream ios = new ByteArrayOutputStream()) {
            byte[] tmp = new byte[512];
            int ch = 0;
//            while (-1 != (ch = is.read())) {
//                ios.write(ch);
//            }
            while (-1 != (ch = is.read(tmp))) {
                ios.write(tmp, 0, ch);
            }
            bytes = ios.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bytes;

    }

    public static void main(String[] args) {
        ClassLoadTest16 test = new ClassLoadTest16("test16");
        ClassLoadTest16 test2 = new ClassLoadTest16("test162");
        try {
            Class<?> cls = test.loadClass("com.leecode.array.Solution_1");
            Object obj = cls.newInstance();
            Class<?> cls2 = test2.loadClass("com.leecode.array.Solution_1");
            Object obj2 = cls2.newInstance();
            obj.getClass().getMethod("test", String.class).invoke(obj, "Hello World.");
            System.out.println(obj);
            System.out.println("cls1: " + cls.hashCode());
            System.out.println("cls2: " + cls2.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
