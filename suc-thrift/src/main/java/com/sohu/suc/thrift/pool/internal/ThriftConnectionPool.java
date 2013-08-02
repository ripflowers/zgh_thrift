package com.sohu.suc.thrift.pool.internal;

import com.sohu.suc.thrift.pool.ConnectionPool;
import com.sohu.suc.thrift.pool.ThriftPoolObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.apache.thrift.transport.TFramedTransport;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * thrift 连接池实现
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 16:46
 */

public class ThriftConnectionPool implements ConnectionPool, InitializingBean, DisposableBean {

    private static final Logger logger = Logger.getLogger(ThriftConnectionPool.class);

//    /**
//     * 服务的IP地址
//     */
//    private String serviceIP;
//    /**
//     * 服务的端口
//     */
//    private int servicePort;

    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 连接超时配置
     */
    private int conTimeOut = 2000;
    /**
     * 可以从缓存池中分配对象的最大数量
     */
    private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;
    /**
     * 缓存池中最大空闲对象数量
     */
    private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;
    /**
     * 缓存池中最小空闲对象数量
     */
    private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;
    /**
     * 阻塞的最大数量
     */
    private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

    private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;
    private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;
    private boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;
    /**
     * 对象缓存池
     */
    private ObjectPool objectPool = null;

    /**
     * /**
     * 取链接池中的一个链接
     *
     * @return
     */
    @Override
    public TFramedTransport getConnection() {
        try {
            TFramedTransport tFramedTransport = (TFramedTransport) objectPool.borrowObject();
            return tFramedTransport;
        } catch (Exception e) {
            throw new RuntimeException("error getConnection()", e);
        }
    }

    /**
     * 返回链接
     *
     * @param tFramedTransport
     */
    @Override
    public void returnCon(TFramedTransport tFramedTransport) {
        try {
            objectPool.returnObject(tFramedTransport);
        } catch (Exception e) {
            throw new RuntimeException("error returnCon()", e);
        }
    }

    /**
     * 不可以轻易调用
     */
    @Override
    public void close() {

        try {
            objectPool.clear();
            objectPool.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        try {
            objectPool.close();
        } catch (Exception e) {
            throw new RuntimeException("error destroy()", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 对象池
        objectPool = new GenericObjectPool();
        //
        ((GenericObjectPool) objectPool).setMaxActive(maxActive);
        ((GenericObjectPool) objectPool).setMaxIdle(maxIdle);
        ((GenericObjectPool) objectPool).setMinIdle(minIdle);
        ((GenericObjectPool) objectPool).setMaxWait(maxWait);
        ((GenericObjectPool) objectPool).setTestOnBorrow(testOnBorrow);
        ((GenericObjectPool) objectPool).setTestOnReturn(testOnReturn);
        ((GenericObjectPool) objectPool).setTestWhileIdle(testWhileIdle);
        ((GenericObjectPool) objectPool)
                .setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
        // 设置factory
//        SucThriftServer sucThriftServer = ThriftServiceLocator.getRandomSwiftServer(serverName);
//        ThriftPoolObjectFactory thriftPoolObjectFactory = new ThriftPoolObjectFactory(
//                sucThriftServer.getHost(), sucThriftServer.getPort(), conTimeOut);
        ThriftPoolObjectFactory thriftPoolObjectFactory = new ThriftPoolObjectFactory(serverName, conTimeOut);
        objectPool.setFactory(thriftPoolObjectFactory);
    }

//    public String getServiceIP() {
//        return serviceIP;
//    }
//
//    public void setServiceIP(String serviceIP) {
//        this.serviceIP = serviceIP;
//    }
//
//    public int getServicePort() {
//        return servicePort;
//    }
//
//    public void setServicePort(int servicePort) {
//        this.servicePort = servicePort;
//    }

    public int getConTimeOut() {
        return conTimeOut;
    }

    public void setConTimeOut(int conTimeOut) {
        this.conTimeOut = conTimeOut;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public ObjectPool getObjectPool() {
        return objectPool;
    }

    public void setObjectPool(ObjectPool objectPool) {
        this.objectPool = objectPool;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
