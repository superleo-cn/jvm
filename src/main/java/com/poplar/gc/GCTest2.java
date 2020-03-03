package com.poplar.gc;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

/**
 * Created BY poplar ON 2019/11/27
 * <p>
 * -verbose:gc 输出冗余的gc信息
 * -Xms20M 堆初始化大最小容量
 * -Xmx20M 堆初始化最大容量
 * -Xmn10M 新生代容量
 * -XX:+PrintGCDetails
 * -XX:SurvivorRatio=8 配置新生代和survivor的大小比例为8：1：1
 * -XX:PretenureSizeThreshold=4194304 设置对象超过多大时直接分配到老年代
 * -XX:+UseSerialGC 表示指定垃圾收集器为SerialGC
 * <p>
 * <p>
 * 使用参数 java -XX:+PrintVMOptions -XX:+AggressiveOpts -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+PrintFlagsFinal  -version | grep PretenureSizeThreshold
 * 可以把当前JVM支持的所有生产和实验行的参数都打印出来，查看默认值
 * <p>
 * 从中可得PretenureSizeThreshold默认值是0，表示没有大对象的预设值要直接晋升到老年代，主要还是靠通过垃圾回收的次数来移动的
 */
public class GCTest2 {

    public static void main(String[] args) {
        int size = 1024 * 1024;
        //GC发生在对象创建时，由于空间不足，JVM就会尝试执行垃圾回收，如果回收后空间还是不足，直接抛出异常OutOfMemoryError: Java heap space
        byte[] bytes1 = new byte[7 * size];

        // 通过API查看内存使用情况
        for (MemoryPoolMXBean poolMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
            System.out.println(poolMXBean.getName());
            System.out.println(poolMXBean.getUsage().getUsed());
        }

        // 如果此时使用VisualVM去监控该进程时，由于VisualVM本身attach的过程中也会创建对象并且消耗内存，总共20m的堆可能就出现空间不够了，会出现gc现象
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello world");
    }
}
