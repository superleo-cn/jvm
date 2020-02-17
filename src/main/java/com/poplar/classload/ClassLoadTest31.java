package com.poplar.classload;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * Created By poplar on 2019/11/9
 */
public class ClassLoadTest31 {

    public static void main(String[] args) throws Exception {
//        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
//        Class<?> clazz2 = Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "123456");

    }
}
