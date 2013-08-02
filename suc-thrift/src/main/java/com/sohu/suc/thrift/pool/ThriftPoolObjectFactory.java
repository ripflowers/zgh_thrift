package com.sohu.suc.thrift.pool;

import com.sohu.suc.thrift.config.ThriftServiceLocator;
import com.sohu.suc.thrift.gen.SucThriftServer;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.log4j.Logger;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 16:50
 */
public class ThriftPoolObjectFactory implements PoolableObjectFactory {

    private static final Logger logger = Logger.getLogger(ThriftPoolObjectFactory.class);

//    /**
//     * 服务的IP
//     */
//    private String serviceIP;
//    /**
//     * 服务的端口
//     */
//    private int servicePort;
    /**
     * 超时设置
     */
    private int timeOut;

    private String serverName;

    public ThriftPoolObjectFactory(String serverName, int timeOut) {
        this.timeOut = timeOut;
        this.serverName = serverName;
    }

//    public ThriftPoolObjectFactory(String serviceIP, int servicePort, int timeOut) {
//        this.serviceIP = serviceIP;
//        this.servicePort = servicePort;
//        this.timeOut = timeOut;
//    }


    @Override
    public Object makeObject() throws Exception {
        try {
            SucThriftServer sucThriftServer = ThriftServiceLocator.getRandomSwiftServer(serverName);
            TTransport transport = new TFramedTransport(new TSocket(sucThriftServer.getHost(), sucThriftServer.getPort(), this.timeOut));
//            TTransport transport = new TSocket(this.serviceIP, this.servicePort, this.timeOut);
            transport.open();
            return transport;
        } catch (Exception e) {
            logger.error("error ThriftPoolObjectFactory()", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroyObject(Object arg0) throws Exception {
        if (arg0 instanceof TFramedTransport) {
            TFramedTransport socket = (TFramedTransport) arg0;
            if (socket.isOpen()) {
                socket.close();
            }
        }
    }

    @Override
    public boolean validateObject(Object arg0) {
        try {
            if (arg0 instanceof TFramedTransport) {
                TFramedTransport thriftSocket = (TFramedTransport) arg0;
                if (thriftSocket.isOpen()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void activateObject(Object o) throws Exception {
        // do nothing ...
    }

    @Override
    public void passivateObject(Object o) throws Exception {
        // do nothing ...
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

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}
