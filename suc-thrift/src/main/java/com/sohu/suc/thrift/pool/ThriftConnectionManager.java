package com.sohu.suc.thrift.pool;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.apache.thrift.transport.TFramedTransport;

/**
 * thrift 连接管理器
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 17:02
 */

public class ThriftConnectionManager implements MethodInterceptor {

    private static final Logger logger = Logger.getLogger(ThriftConnectionManager.class);

    /**
     * 保存local对象
     */
    ThreadLocal<TFramedTransport> tFramedTransportThreadSafe = new ThreadLocal<TFramedTransport>();
    /**
     * 连接提供池
     */
    private ConnectionPool connectionPool;

    @Override
    public Object invoke(MethodInvocation arg0) throws Throwable {
        TFramedTransport tFramedTransport = null;
        try {
            tFramedTransport = connectionPool.getConnection();
            tFramedTransportThreadSafe.set(tFramedTransport);
            // aop 切入
            Object o = arg0.proceed();
           
            return o;
        } catch (Exception e) {
            tFramedTransport.close();
            logger.error("error ThriftConnectionManager.invoke()", e);
            throw new Exception(e);
        } finally {
            connectionPool.returnCon(tFrameTransport);
            tFramedTransportThreadSafe.remove();
        }

    }

    /**
     * 取socket
     *
     * @return
     */
    public TFramedTransport getSocket() {
        return tFramedTransportThreadSafe.get();
    }

    public void close() {
        connectionPool.close();
    }


    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
