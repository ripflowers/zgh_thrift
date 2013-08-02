package com.sohu.suc.thrift;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 测试一下并发情况下的对象池的表现
 * 发现objectPool 是线程安全的.
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-25 16:27
 */
public class TestObjectPoolThreadSafe {

    public static void main(String... args) throws InterruptedException {
        final GenericObjectPool baseObjectPool = new GenericObjectPool(new PoolableObjectFactory() {
            @Override
            public Object makeObject() throws Exception {
                System.out.println("generate one object!");
                return new Random().nextInt(10);
            }

            @Override
            public void destroyObject(Object obj) throws Exception {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean validateObject(Object obj) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void activateObject(Object obj) throws Exception {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void passivateObject(Object obj) throws Exception {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        baseObjectPool.setMaxActive(50);

        final CountDownLatch countDownLatch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Integer i = (Integer) baseObjectPool.borrowObject();
                        System.out.println("Borrow object is " + i);
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        countDownLatch.await();

        System.out.println("done !");


    }

}
