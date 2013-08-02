package com.sohu.suc.thrift.server;

import com.sohu.suc.thrift.gen.PersonService;
import com.sohu.suc.thrift.regist.ThriftServiceRegister;
import com.sohu.suc.thrift.server.internal.PersonServiceImpl;
import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 16:07
 */
public class PersonThriftServer {

    private static final Logger logger = Logger.getLogger(PersonThriftServer.class);

    /**
     * 启动参数
     * -Dswift.ip=10.2.145.116
     * -Dswift.port=7913
     * -Dswift.serverName=zghTest
     * -Dswift.weight=2
     *
     * @param args
     * @throws TTransportException
     */
    public static void main(String... args) throws TTransportException {
        // 设置服务端口为 7913
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(7913);
        // 设置协议工厂为 TBinaryProtocol.Factory
        TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();
        TProcessor processor = new PersonService.Processor<PersonServiceImpl>(new PersonServiceImpl());
        TServer server = new TThreadedSelectorServer(new TThreadedSelectorServer.Args(serverTransport).processor(processor)
                .protocolFactory(proFactory).selectorThreads(10).workerThreads(10));
        logger.info("Start server on port 7913...");
        //注册
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ThriftServiceRegister thriftServiceRegister = (ThriftServiceRegister) applicationContext.getBean("thriftServiceRegisterImpl");
        thriftServiceRegister.publish();
        server.serve();
    }

}
