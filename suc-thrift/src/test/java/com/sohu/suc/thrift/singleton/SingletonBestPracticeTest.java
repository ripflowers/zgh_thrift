package com.sohu.suc.thrift.singleton;

import java.util.concurrent.CountDownLatch;

/**
 * 测试一下单例模式的最佳实践
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-26 12:23
 */
public class SingletonBestPracticeTest {

    public static void main(String... args) throws InterruptedException {
//        SingletonBestPractice s1 = SingletonBestPractice.getInstances();
//        SingletonBestPractice s2 = SingletonBestPractice.getInstances();
//
//        System.out.println(s1 == s2);

        final SingletonBestPractice[] s1 = {null};
        final SingletonBestPractice[] s2 = {null};

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                s1[0] = SingletonBestPractice.getInstances();
                countDownLatch.countDown();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                s2[0] = SingletonBestPractice.getInstances();
                countDownLatch.countDown();
            }
        });

        thread.start();
        thread2.start();

        countDownLatch.await();

        System.out.println(s1[0] == s2[0]);

    }

}
