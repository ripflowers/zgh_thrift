package com.sohu.suc.thrift.regist;

import com.google.common.base.Preconditions;
import com.sohu.suc.thrift.gen.SucThriftServer;
import com.sohu.suc.thrift.regist.utils.IpUtil;
import com.sohu.suc.thrift.regist.utils.SystemConfig;
import com.sohu.suc.thrift.regist.utils.ThriftServerFunctionUtil;
import com.sohu.suc.thrift.regist.utils.ZkPathUtil;
import com.sohu.suc.zkclient.ZkClient;
import com.sohu.suc.zkclient.ZkMap;
import com.sohu.suc.zkclient.ZkMapSetter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 注册swiftServer的实现类
 * 注册到zk中实际上是一个{@link com.sohu.suc.thrift.gen.SucThriftServer}的二进制数据
 * SwiftServer的属性说明
 * 1，host server运行机器的ip   取eth0
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-17 15:13
 */

@Service
public class ThriftServiceRegisterImpl implements ThriftServiceRegister {

    private static final Logger logger = Logger.getLogger(ThriftServiceRegisterImpl.class);

    private static final int DEFAULT_PORT = 13800;
    private static final int DEFAULT_WEIGHT = 1;

    @Override
    public void publish() {
        String path = getSwiftServerRegisterPath();
        String host = getSwiftServerRegisterIp();
        int port = getSwiftServerRegisterPort();
        String serverName = getSwiftServerRegisterName();
        int weight = getSwiftServerRegisterWeight();
        long startTime = System.currentTimeMillis();

        SucThriftServer swiftServer = new SucThriftServer();
        swiftServer.setHost(host);
        swiftServer.setPort(port);
        swiftServer.setServerName(serverName);
        swiftServer.setWeight(weight);
        swiftServer.setStartTime(startTime);

//      在注册中心发布服务开始

        String registryPath = new StringBuilder().append(path).append("/").append(serverName).toString();
        ThriftServerFunctionUtil.ThriftServerToByteArray swiftServerToByteArray = new ThriftServerFunctionUtil.ThriftServerToByteArray();
        ThriftServerFunctionUtil.ByteArrayToThriftServer byteArrayToThriftServer = new ThriftServerFunctionUtil.ByteArrayToThriftServer();
        ZkClient zkClient = new ZkClient(ZkPathUtil.swiftZkConnect, null, null);
        //非持久化节点
        ZkMapSetter<SucThriftServer> zkMapSetter = new ZkMapSetter(zkClient, registryPath, swiftServerToByteArray, false);
        zkMapSetter.put(new StringBuilder().append(host).append(":").append(port).toString(), swiftServer);
        ZkMap<SucThriftServer> zkMap = ZkMap.createZkMap(zkClient, registryPath, byteArrayToThriftServer);
        logger.info("server list :" + zkMap.delegate());
    }

    /**
     * 获取thrift Server的端口号，以参数 -Dswift.port=1234 传递
     *
     * @return port
     */
    private int getSwiftServerRegisterPort() {
        int port = SystemConfig.getInstance().getInt("swift.port", DEFAULT_PORT);
        if (port > 0 && port < 65535) {
            return port;
        } else {
            return DEFAULT_PORT;
        }
    }

    /**
     * 通过thrift Server 启动时传递的 -Dswift.weight=1 传递
     * <p/>
     * 默认为1，范围是（1-10）
     *
     * @return
     */
    private int getSwiftServerRegisterWeight() {
        int weight = SystemConfig.getInstance().getInt("swift.weight", DEFAULT_WEIGHT);
        if (weight <= 0) {
            weight = 1;
        }
        if (weight > 100) {
            weight = 100;
        }
        return weight;
    }

    /**
     * swift Server 的业务逻辑名称   通过-Dswift.serverName=Saccounts 传递，不可以为空！
     *
     * @return server name
     */
    private String getSwiftServerRegisterName() {
        String serverName = SystemConfig.getInstance().getString("swift.serverName", "");
        Preconditions.checkArgument(StringUtils.isNotBlank(serverName), "server name must not be blank!!");
        return serverName;
    }

    /**
     * 获取本机ip 通过-Dswift.ip=127.0.0.1来传递，如果没有传递参数，那么取第一块网卡的ip（ipv4）
     *
     * @return ip
     */
    private String getSwiftServerRegisterIp() {
        String ip = IpUtil.getIp();
        Preconditions.checkArgument(StringUtils.isNotBlank(ip), "ip can not be blank !");
        return ip;
    }

    /**
     * 根据系统属性 获得在zk中注册的路径
     *
     * @return
     */
    private String getSwiftServerRegisterPath() {
        return ZkPathUtil.getSwiftRegistryRootPath();
    }
}
