package com.sohu.suc.thrift.pool;

import org.apache.thrift.transport.TFramedTransport;

/**
 * 连接池接口
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 16:44
 */
public interface ConnectionPool {

    /**
     * 取链接池中的一个链接
     *
     * @return
     */
    public TFramedTransport getConnection();

    /**
     * 返回链接
     *
     * @param tFramedTransport
     */
    public void returnCon(TFramedTransport tFramedTransport);

    /**
     * 不可以轻易调用
     */
    public void close();

}
