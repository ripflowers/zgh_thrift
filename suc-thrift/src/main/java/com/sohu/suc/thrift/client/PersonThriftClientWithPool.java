package com.sohu.suc.thrift.client;

import com.sohu.suc.thrift.gen.Person;
import com.sohu.suc.thrift.gen.PersonService;
import com.sohu.suc.thrift.pool.ThriftConnectionManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 17:15
 */
public class PersonThriftClientWithPool {

    private static final Logger logger = Logger.getLogger(PersonThriftClientWithPool.class);

    public void setThriftConnectionManager(ThriftConnectionManager thriftConnectionManager) {
        this.thriftConnectionManager = thriftConnectionManager;
    }

    private ThriftConnectionManager thriftConnectionManager;

    public String personToString(Person person) throws TException {
        TProtocol protocol = new TBinaryProtocol(thriftConnectionManager.getSocket());
        PersonService.Client client = new PersonService.Client(protocol);
        logger.info(client.personToString(person));
        return "server genarated " + client.personToString(person);
    }

    public void close(){
        thriftConnectionManager.close();
    }

}
