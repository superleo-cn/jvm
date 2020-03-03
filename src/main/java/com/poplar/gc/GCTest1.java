package com.poplar.gc;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * 1、首先，GC又分为minor GC 和 Full GC（major GC）。Java堆内存分为新生代和老年代，新生代中又分为1个eden区和两个Survior区域。
 * <p>
 * 2、一般情况下，新创建的对象都会被分配到eden区，这些对象经过一个minor gc后仍然存活将会被移动到Survior区域中，对象在Survior中没熬过一个Minor GC，年龄就会增加一岁，当他的年龄到达一定程度时，就会被移动到老年代中。
 * <p>
 * 3、当eden区满时，还存活的对象将被复制到survior区，当一个survior区满时，此区域的存活对象将被复制到另外一个survior区，当另外一个也满了的时候，从前一个Survior区复制过来的并且此时还存活的对象，将可能被复制到老年代。因为年轻代中的对象基本都是朝生夕死（80%以上），所以年轻代的垃圾回收算法使用的是复制算法，复制算法的基本思想是将内存分为两块，每次只有其中一块，当这一块内存使用完，就将还活着的对象复制到另一块上面。复制算法不会产生内存碎片。
 * <p>
 * 4、在GC开始的时候，对象只会存在于eden区，和名为“From”的Survior区，Survior区“to”是空的。紧接着GCeden区中所有存活的对象都会被复制到“To”,而在from区中，仍存活的对象会根据他们的年龄值来决定去向，年龄到达一定只的对象会被复制到老年代,没有到达的对象会被复制到to survior中，经过这次gc后，eden区和fromsurvior区已经被清空。这个时候，from和to会交换他们的角色，也就是新的to就是上次GC前的fromMinor GC：从年轻代回收内存。
 * <p>
 * 5、当jvm无法为一个新的对象分配空间时会触发Minor GC，比如当Eden区满了。当内存池被填满的时候，其中的内容全部会被复制，指针会从0开始跟踪空闲内存。Eden和Survior区不存在内存碎片写指针总是停留在所使用内存池的顶部。执行minor操作时不会影响到永久代，从永久带到年轻代的引用被当成GC roots，从年轻代到永久代的引用在标记阶段被直接忽略掉（永久代用来存放java的类信息）。如果eden区域中大部分对象被认为是垃圾，永远也不会复制到Survior区域或者老年代空间。如果正好相反，eden区域大部分新生对象不符合GC条件，Minor GC执行时暂停的线程时间将会长很多。Minor may call "stop the world"。
 * <p>
 * Created BY poplar ON 2019/11/27
 * 垃圾回收测试
 */
public class GCTest1 {

    /*
    -verbose:gc 输出冗余的gc信息，不包括详细的垃圾回收信息
    -Xms20M 堆初始化大最小容量
    -Xmx20M 堆初始化最大容量
    -Xmn10M 新生代容量
    -XX:+PrintGCDetails 详细的垃圾回收打印信息
    -XX:SurvivorRatio=8 配置新生代和survivor的大小比例为8：1：1
    */

    public static void main(String[] args) {
        printGCList();
        System.out.println("=================");
        int size = 1024 * 1024;
        System.out.println("000000");
        byte[] bytes1 = new byte[2 * size];
        System.out.println("111111");
        byte[] bytes2 = new byte[2 * size];
        System.out.println("222222");
        byte[] bytes3 = new byte[2 * size];
        System.out.println("333333");
        byte[] bytes4 = new byte[2 * size];
        /*

        为啥byte3和byte4都设置2M 会执行 fullGC 呢？
        首先默认情况下使用的是UseParallelGC，里面涉及到一个Ergonomics，简单可以理解为为了达到最大吞吐量而自适应调整对硬堆的大小。
        如果使用UseSerialGC或UseParNewGC就不会有FullGC出现了

        在发生MinorGC之前，虚拟机会先检查老年代最大可利用的连续空间是否大于新生代所有对象的总空间。
        如果大于则进行Minor GC，如果小于则看HandlePromotionFailure设置是否是允许担保失败（不允许则直接FullGC）
        如果允许，那么会继续检查老年代最大可利用的连续空间是否大于历次晋升到老年代对象的平均大小，如果大于
        则尝试minor gc （如果尝试失败也会触发Full GC），如果小于则进行Full GC。
        这里full gc的reason是Ergonomics，是因为开启了UseAdaptiveSizePolicy，jvm自己进行自适应调整引发的full gc: 目的是减少Minor GC的次数
        因为YoungGen的使用率已经很高了

        注意: 如果此时把Xmx改大，也能避免UseParallelGC下导致的GC情况。

        -XX:+UseAdaptiveSizePolicy配合参数-XX:AdaptiveSizePolicyOutputInterval=N(N表示每次间隔次数打印输出)来查看详细情况
        默认情况下，一个代增长或缩小是按照固定百分比，这样有助于达到指定大小。默认增加以 20% 的速率，缩小以 5%。也可以自己设定
         */

           /*
        为啥byte3和byte4都设置3M就不会执行 fullGC 呢？
        因为2+2+3=7M加上系统自动生成的一些对象，在Eden假设是足够保存了。接下来在分配一个3m的byte4，是无论如何也存不下了，这个时候根据担保分配机制
        就直接跳过新生代，到了老年代进行分配了
         */
        System.out.println("444444");
        //当需要分配内存的对象的大小超出了新生代的容量时，对象会被直接分配到老年代
//        System.out.println("hello world");

        /*
         [GC (Allocation Failure)（表示发生GC的原因） [PSYoungGen（PS表示收集器类型）: 8144K（收集前）->728K（收集后）(9216K)
         （新生代总的容量）] 8144K（推收集前）->6872K（堆收集后）(19456K)（堆总的容量）, 0.0087417 secs（所用时间）] [Times: user（用户态收集所用时间）=0.02 sys=0.02系统态收集所用时间）, real=0.01 secs]
         [Full GC (Ergonomics) [PSYoungGen: 728K->0K(9216K)] [ParOldGen: 6144K->6774K(10240K)] 6872K->6774K(19456K), [Metaspace: 3217K->3217K(1056768K)], 0.0070323 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
         hello world
         Heap
         PSYoungGen      total 9216K, used 2287K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         eden space 8192K, 27% used [0x00000000ff600000,0x00000000ff83be00,0x00000000ffe00000)
         from space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
         to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
         ParOldGen       total 10240K, used 6774K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         object space 10240K, 66% used [0x00000000fec00000,0x00000000ff29daa8,0x00000000ff600000)
         Metaspace       used 3225K, capacity 4496K, committed 4864K, reserved 1056768K
         class space    used 350K, capacity 388K, committed 512K, reserved 1048576K
         */
    }

    // 打印 young/old 代默认所使用的 collector
    /*

    PS 开头的系列 collector 是 Java5u6 开始引入的。按照 R 大的说法，这之前的 collector 都是在一个框架内开发的，
    所以 young/old 代的 collector 可以任意搭配，但 PS 系列与后来的 G1 不是在这个框架内的，所以只能单独使用。

    使用 UseSerialGC 时 young 代的 collector 是 Copy，这是单线程的，PS Scavenge 与 ParNew 分别对其并行化，至于这两个并行 young 代 collector 的区别呢？这里再引用 R 大的回复：
	1.	PS以前是广度优先顺序来遍历对象图的，JDK6的时候改为默认用深度优先顺序遍历，并留有一个UseDepthFirstScavengeOrder参数来选择是用深度还是广度优先。在JDK6u18之后这个参数被去掉，PS变为只用深度优先遍历。ParNew则是一直都只用广度优先顺序来遍历
	2.	PS完整实现了adaptive size policy，而ParNew及“分代式GC框架”内的其它GC都没有实现完（倒不是不能实现，就是麻烦+没人力资源去做）。所以千万千万别在用ParNew+CMS的组合下用UseAdaptiveSizePolicy，请只在使用UseParallelGC或UseParallelOldGC的时候用它。
	3.	由于在“分代式GC框架”内，ParNew可以跟CMS搭配使用，而ParallelScavenge不能。当时ParNew GC被从Exact VM移植到HotSpot VM的最大原因就是为了跟CMS搭配使用。
	4.	在PS成为主要的throughput GC之后，它还实现了针对NUMA的优化；而ParNew一直没有得到NUMA优化的实现。
如果你对上面所说的 mark/sweep/compact 这些名词不了解，建议参考下面这篇文章：
	•	https://plumbr.io/handbook/garbage-collection-algorithms-implementations


     */
    public static void printGCList() {
        try {
            List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
            for (GarbageCollectorMXBean gcMxBean : gcMxBeans) {
                System.out.println(gcMxBean.getName());
            }
        } catch (Exception exp) {
            System.err.println(exp);
        }

    }
}
