package com.sohu.suc.thrift;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.util.Random;

/**
 * 测试一下apache Commons 提供的对象池
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-25 16:13
 */
public class TestObjectPool {

    public static void main(String... args) throws Exception {
        ObjectPool objectPool = new GenericObjectPool(new PoolableObjectFactory() {
            @Override
            public Object makeObject() throws Exception {
                System.out.println("make one object!!");
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

        Integer i = (Integer) objectPool.borrowObject();
        Integer i2 = (Integer) objectPool.borrowObject();
        Integer i3 = (Integer) objectPool.borrowObject();

        objectPool.returnObject(i);
        objectPool.returnObject(i2);
        objectPool.returnObject(i3);

        System.out.println(i);
        System.out.println(i2);
        System.out.println(i3);

    }

}
