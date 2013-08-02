package com.sohu.suc.thrift.config;

import com.google.common.collect.Maps;
import com.sohu.suc.thrift.gen.SucThriftServer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: guohaozhao (guohaozhao@sohu-inc.com)
 * Date: 13-7-3 18:07
 */
public class ThriftServiceLocatorT {

    private static final Logger logger = Logger.getLogger(ThriftServiceLocatorT.class);

    public static void main(String... args) throws Exception {
        SucThriftServer thriftServer = new SucThriftServer();
        SucThriftServer thriftServer1 = new SucThriftServer();
        SucThriftServer thriftServer2 = new SucThriftServer();

        thriftServer.setServerName("0");
        thriftServer.setWeight(1);
        thriftServer1.setServerName("1");
        thriftServer1.setWeight(2);
        thriftServer2.setServerName("3");
        thriftServer2.setWeight(3);

        Map<String, SucThriftServer> swiftServerMap = Maps.newConcurrentMap();
        swiftServerMap.put("0", thriftServer);
        swiftServerMap.put("1", thriftServer1);
        swiftServerMap.put("2", thriftServer2);

        List<SucThriftServer> sucThriftServers = new ArrayList<SucThriftServer>();

        for (SucThriftServer sucThriftServer : swiftServerMap.values()) {
            int weight = sucThriftServer.getWeight();
            for (int i = 0; i < weight; i++) {
                sucThriftServers.add(sucThriftServer);
            }
        }

        System.out.println(sucThriftServers.size());


    }
}
