package com.sohu.suc.thrift.client;

import com.sohu.suc.thrift.config.ThriftServiceLocator;
import com.sohu.suc.thrift.gen.Person;
import com.sohu.suc.thrift.gen.PersonService;
import com.sohu.suc.thrift.gen.SucThriftServer;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 16:11
 */
public class PersonThriftClient {

    private static Logger logger = Logger.getLogger(PersonThriftClient.class);

    public static void main(String... args) throws TException {
        SucThriftServer thriftServer = ThriftServiceLocator.getRandomSwiftServer("zghTest");
        TTransport transport = new TFramedTransport(new TSocket(thriftServer.getHost(), thriftServer.getPort()));
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);
        Person person = new Person();
        person.setName("babyduncan21");
        person.setAge(21);
        logger.info(client.personToString(person));
        transport.close();
    }

}
