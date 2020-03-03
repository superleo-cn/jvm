package com.poplar.gc;

/**
 * Created BY poplar ON 2019/11/28
 */
public class CMSGCTest {
    public static void main(String[] args) {
        int size = 1024 * 1024;
        byte[] bytes1 = new byte[4 * size];
        System.out.println("111111");

        byte[] bytes2 = new byte[4 * size];
        System.out.println("2222222");

        byte[] bytes3 = new byte[4 * size];
        System.out.println("33333333");

        try{
            // 如果此时是2m的字节，程序是没有问题的。如果是4m字节大小。由于对象不能跨代进行存储，
            // 因此无论是新生代8M的Eden+1M的survivor，最多只能放下一个4m的对象（还有一些其他默认启动的jvm对象也会随着创建，所以实际用不到8m）
            // 还是老年代10m，也没法保存12m的对象
            byte[] bytes4 = new byte[2 * size];
            System.out.println("4444444");

        } catch (Error e) {
            e.printStackTrace();

        }

        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
