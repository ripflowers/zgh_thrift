package com.sohu.suc.thrift.config;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sohu.suc.thrift.gen.SucThriftServer;
import com.sohu.suc.thrift.regist.utils.ThriftServerFunctionUtil;
import com.sohu.suc.thrift.regist.utils.ZkPathUtil;
import com.sohu.suc.zkclient.ZkClient;
import com.sohu.suc.zkclient.ZkMap;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * swiftServer 服务选择器
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-17 14:55
 */
public final class ThriftServiceLocator {

    private ThriftServiceLocator() {
    }

    private static final Logger logger = Logger.getLogger(ThriftServiceLocator.class);

    private static HashMap<String, ZkMap<SucThriftServer>> zkMap = null;
    //    private static byte[] lock = new byte[0];
    private static Lock lock = new ReentrantLock();

    private static Map<String, SucThriftServer> getSwiftServers(String serverName) {
        if (zkMap != null) {
            return zkMap.get(serverName);
        } else {
            lock.lock();
            try {
                if (zkMap != null) {
                    return zkMap.get(serverName);
                }
                ThriftServerFunctionUtil.ByteArrayToThriftServer byteArrayToThriftServer =
                        new ThriftServerFunctionUtil.ByteArrayToThriftServer();

                ThriftServerFunctionUtil.ByteArrayToString byteArrayToString =
                        new ThriftServerFunctionUtil.ByteArrayToString();

                ZkClient zkClient = new ZkClient(ZkPathUtil.swiftZkConnect, null, null);
                String path = ZkPathUtil.getSwiftRegistryRootPath();
                ZkMap<String> thriftServers = ZkMap.createZkMap(zkClient, path, byteArrayToString);
                zkMap = new HashMap<String, ZkMap<SucThriftServer>>();
                for (String s : thriftServers.delegate().keySet()) {
                    String registryPath = new StringBuilder().append(path).append("/").append(s).toString();
                    ZkMap<SucThriftServer> _zkMap = ZkMap.createZkMap(zkClient, registryPath, byteArrayToThriftServer);
                    zkMap.put(s, _zkMap);
                }
                logger.info("connected to zk !");
                return zkMap.get(serverName);
            } finally {
                lock.unlock();
            }
        }


//        if (zkMap != null) {
//            return zkMap.delegate();
//        } else {
//            synchronized (lock) {
//                if (zkMap != null) {
//                    return zkMap.delegate();
//                } else {
//                    SwiftServerFunctionUtil.ByteArrayToSwiftServer byteArrayToSwiftServer = new SwiftServerFunctionUtil.ByteArrayToSwiftServer();
//                    ZkClient zkClient = new ZkClient(ZkPathUtil.swiftZkConnect, null, null);
//                    String path = ZkPathUtil.getSwiftRegistryRootPath();
//                    String registryPath = new StringBuilder().append(path).append("/").append(serverName).toString();
//                    zkMap = ZkMap.createZkMap(zkClient, registryPath, byteArrayToSwiftServer);
//                    return zkMap.delegate();
//                }
//            }
//        }
    }

    public static SucThriftServer getRandomSwiftServer(String serverName) {
        Map<String, SucThriftServer> swiftServerMap = getSwiftServers(serverName);
//        logger.info("server size is " + swiftServerMap.size());
        int size = swiftServerMap.size();
        int random = 0;
        if (size != 0) {
            List serverList = new ArrayList<SucThriftServer>();
            for (SucThriftServer sucThriftServer : swiftServerMap.values()) {
                int weight = sucThriftServer.getWeight();
                if (weight <= 0) {
                    weight = 1;
                }
                if (weight > 100) {
                    weight = 100;
                }
                for (int i = 0; i < weight; i++) {
                    serverList.add(sucThriftServer);
                }
            }
            random = new Random().nextInt(serverList.size());
            return (SucThriftServer) serverList.get(random);
        } else {
            return null;
        }
    }

    /**
     * 通过特定的系统属性,来指定thriftServer
     * 通过-Dswift.specificServerName 来制定serverName
     * 通过-Dswift.specificIp 指定ip,通过 -Dswift.specificPort 来指定端口
     *
     * @return
     */
    private SucThriftServer getSpecificSwiftServer() {
        String serverName = System.getProperty("swift.specificServerName");
        String ip = System.getProperty("swift.specificIp");
        if (!Strings.isNullOrEmpty(serverName) && !Strings.isNullOrEmpty(ip)) {
            int port = 7913;
            try {
                port = Integer.parseInt(System.getProperty("swift.specificPort"));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            SucThriftServer sucThriftServer = new SucThriftServer();
            sucThriftServer.setHost(ip);
            sucThriftServer.setPort(port);
            sucThriftServer.setServerName(serverName);
            sucThriftServer.setWeight(1);
            sucThriftServer.setStartTime(System.currentTimeMillis());
            return sucThriftServer;
        } else {
            return null;
        }

    }


}
