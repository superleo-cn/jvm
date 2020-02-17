package com.poplar.gc;

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
        在发生MinorGC之前，虚拟机会先检查老年代最大可利用的连续空间是否大于新生代所有对象的总空间。
        如果大于则进行Minor GC，如果小于则看HandlePromotionFailure设置是否是允许担保失败（不允许则直接FullGC）
        如果允许，那么会继续检查老年代最大可利用的连续空间是否大于历次晋升到老年代对象的平均大小，如果大于
        则尝试minor gc （如果尝试失败也会触发Full GC），如果小于则进行Full GC。
        这里full gc的reason是Ergonomics，是因为开启了UseAdaptiveSizePolicy，jvm自己进行自适应调整引发的full gc: 目的是减少Minor GC的次数
        因为YoungGen的使用率已经很高了
         */


           /*
        为啥byte3和byte4都设置3M就不会执行 fullGC 呢？
        因为2+2+3=7M加上系统自动生成的一些对象，在Eden假设是足够保存了。接下来在分配一个3m的byte4，是无论如何也存不下了，这个时候就直接让
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
}
