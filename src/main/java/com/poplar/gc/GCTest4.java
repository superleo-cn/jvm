package com.poplar.gc;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * Created BY poplar ON 2019/11/28
 * -verbose:gc
 * -Xmx200M
 * -Xmn50M
 * -XX:TargetSurvivorRatio=60 表明所有age的survivor space对象的大小如果超过Desired survivor size，则重新计算threshold
 * -XX:+PrintTenuringDistribution 打印对象年龄
 * -XX:+PrintGCDetails
 * -XX:+PrintGCDateStamps 打印收集时间
 * -XX:+UseConcMarkSweepGC 老年代使用cms收集器
 * -XX:+UseParNewGC 新生代使用ParNew收集器
 * -XX:MaxTenuringThreshold=3 设置晋升到老年代的阈值
 */
public class GCTest4 {

    public static void main(String[] args) throws InterruptedException {
        byte[] bytes1 = new byte[1024 * 1024];
        byte[] bytes2 = new byte[1024 * 1024];

        method();
        printGCList();
        Thread.sleep(1000);
        System.out.println("11111111");

        method();
        printGCList();
        Thread.sleep(1000);
        System.out.println("222222222");

        method();
        printGCList();
        Thread.sleep(1000);
        System.out.println("3333333333");

        method();
        printGCList();
        Thread.sleep(1000);
        System.out.println("4444444444");

        byte[] bytes3 = new byte[1024 * 1024];
        byte[] bytes4 = new byte[1024 * 1024];
        byte[] bytes5 = new byte[1024 * 1024];

        method();
        printGCList();
        Thread.sleep(1000);
        System.out.println("5555555");

        method();
        printGCList();
        Thread.sleep(1000);
        System.out.println("666666");
    }

    public static void method() {
        for (int i = 0; i < 40; i++) {
            byte[] bytes = new byte[1024 * 1024];
        }
    }

    public static void printGCList() {
        try {
            for (MemoryPoolMXBean poolMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
                System.out.println(poolMXBean.getName() + ": " + poolMXBean.getUsage().getUsed());
            }
        } catch (Exception exp) {
            System.err.println(exp);
        }

    }
}
/*


/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/bin/java -verbose:gc -Xmx200m -Xmn50m -XX:TargetSurvivorRatio=60 -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintGCDateStamps -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:MaxTenuringThreshold=3 "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=64958:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/lib/tools.jar:/Users/kylin/Documents/dev/java/opensource/jvm-learn/out/production/classes:/Users/kylin/.gradle/caches/modules-2/files-2.1/mysql/mysql-connector-java/5.1.38/dbbd7cd309ce167ec8367de4e41c63c2c8593cc5/mysql-connector-java-5.1.38.jar:/Users/kylin/.gradle/caches/modules-2/files-2.1/cglib/cglib/3.3.0/c956b9f9708af5901e9cf05701e9b2b1c25027cc/cglib-3.3.0.jar:/Users/kylin/.gradle/caches/modules-2/files-2.1/org.ow2.asm/asm/7.1/fa29aa438674ff19d5e1386d2c3527a0267f291e/asm-7.1.jar com.poplar.gc.GCTest4
2020-02-19T13:11:10.185-0800: [GC (Allocation Failure) 2020-02-19T13:11:10.185-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:    2499992 bytes,    2499992 total
: 40141K->2489K(46080K), 0.0016100 secs] 40141K->2489K(199680K), 0.0016501 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
Code Cache: 1146752
Metaspace: 3342392
Compressed Class Space: 359216
Par Eden Space: 7104720
Par Survivor Space: 2548896
CMS Old Gen: 0
11111111
2020-02-19T13:11:11.199-0800: [GC (Allocation Failure) 2020-02-19T13:11:11.199-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:     215752 bytes,     215752 total
- age   2:    2490704 bytes,    2706456 total
: 42810K->2783K(46080K), 0.0019148 secs] 42810K->2783K(199680K), 0.0019438 secs] [Times: user=0.01 sys=0.01, real=0.01 secs]
Code Cache: 1187328
Metaspace: 3533136
Compressed Class Space: 377304
Par Eden Space: 11286360
Par Survivor Space: 2850712
CMS Old Gen: 0
222222222
2020-02-19T13:11:12.206-0800: [GC (Allocation Failure) 2020-02-19T13:11:12.206-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:     171976 bytes,     171976 total
- age   2:     185008 bytes,     356984 total
- age   3:    2489264 bytes,    2846248 total
: 43101K->3074K(46080K), 0.0007243 secs] 43101K->3074K(199680K), 0.0007476 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
Code Cache: 1248384
Metaspace: 3857968
Compressed Class Space: 418528
Par Eden Space: 13379832
Par Survivor Space: 3148488
CMS Old Gen: 0
3333333333
2020-02-19T13:11:13.213-0800: [GC (Allocation Failure) 2020-02-19T13:11:13.213-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:         64 bytes,         64 total
- age   2:     171416 bytes,     171480 total
- age   3:     185008 bytes,     356488 total
: 43789K->852K(46080K), 0.0033915 secs] 43789K->3307K(199680K), 0.0034189 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
Code Cache: 1248384
Metaspace: 3857968
Compressed Class Space: 418528
Par Eden Space: 14437576
Par Survivor Space: 872664
CMS Old Gen: 2514560
4444444444


-- 注意下面这一段回收过程：当Survivor不够TargetSurvivorRatio控制的值时(这个例子是3m)。那么就会开始动态调整MaxTenuringThreshold中的值，
然后让存在于survivor中(age1, age2, age3...ageN)中的对象全部都移到老年代中去。
我们也能够清晰的看到之前的步骤中survivor中的存活对象都是没有超过3m的。

2020-02-19T13:11:14.226-0800: [GC (Allocation Failure) 2020-02-19T13:11:14.226-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 1 (max 3)
- age   1:    3145840 bytes,    3145840 total
- age   2:         64 bytes,    3145904 total
- age   3:     171728 bytes,    3317632 total
: 41575K->3297K(46080K), 0.0017728 secs] 44031K->5938K(199680K), 0.0018110 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
Code Cache: 1251072
Metaspace: 3859064
Compressed Class Space: 418528
Par Eden Space: 18637832
Par Survivor Space: 3377040
CMS Old Gen: 2704056
5555555


-- 通过这个日志我们已经可以看到老年代由变大了，与此同时threshold又改成了3。
2020-02-19T13:11:15.238-0800: [GC (Allocation Failure) 2020-02-19T13:11:15.238-0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:         56 bytes,         56 total
: 44027K->18K(46080K), 0.0030149 secs] 46667K->5905K(199680K), 0.0030519 secs] [Times: user=0.02 sys=0.01, real=0.01 secs]
Code Cache: 1253632
Metaspace: 3859784
Compressed Class Space: 418528
Par Eden Space: 19690208
Par Survivor Space: 19288
CMS Old Gen: 6027968
666666
Heap
 par new generation   total 46080K, used 20067K [0x00000007b3800000, 0x00000007b6a00000, 0x00000007b6a00000)
  eden space 40960K,  48% used [0x00000007b3800000, 0x00000007b4b94120, 0x00000007b6000000)
  from space 5120K,   0% used [0x00000007b6000000, 0x00000007b6004b58, 0x00000007b6500000)
  to   space 5120K,   0% used [0x00000007b6500000, 0x00000007b6500000, 0x00000007b6a00000)
 concurrent mark-sweep generation total 153600K, used 5886K [0x00000007b6a00000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 3775K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 409K, capacity 428K, committed 512K, reserved 1048576K

Process finished with exit code 0


 */