package com.sohu.suc.thrift.server.internal;

import com.sohu.suc.thrift.gen.Person;
import com.sohu.suc.thrift.gen.PersonService;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 16:04
 */
public class PersonServiceImpl implements PersonService.Iface {

    private static final Logger logger = Logger.getLogger(PersonServiceImpl.class);

    @Override
    public String personToString(Person person) throws TException {
        return "server genarated " + person.toString();
    }
}
