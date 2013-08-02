package com.sohu.suc.thrift.regist.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取linux 系统中eth0 的ip   在linux系统上可用，在windows系统上不可用
 * 我们的测试环境和生产环境都是linux系统 ，获取eth0用于注册到配置中心。
 * 对于windows机器，应该取不到ip，取到值为"" 。为空的话 就不向注册中心注册了
 * 自己本机调试的话就直连ip即可。
 * <p/>
 * 支持自定义ip地址，形式为：
 * -Dswift.ip=127.0.0.1
 * 优先选择 -Dswift.ip 为注册ip，如果没有填写则使用从系统获得的eth0 （第一块网卡）ip作为server ip
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-17 15:37
 */
public final class IpUtil {

//    如果加上这句，那么反射执行此类的方法也不能，为了单元测试，还是放开一点吧。
//    private IpUtil() {
//    }

    private static final Logger logger = Logger.getLogger(IpUtil.class);

    /**
     * 获取linux（unix） 系统的第一块网卡的ip
     *
     * @return ip
     */
    public static String getLocalIp() {
        String ip = "";
        try {
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                //仅获取eth0
                if (!ni.getName().equals("eth0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        // 不取ipv6
                        if (ia instanceof Inet6Address) {
                            continue;
                        }
                        ip = ia.getHostAddress();
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            logger.error("IpUtil -> getLocalIp error !! " + e.getMessage());
        }
        return ip;
    }

    /**
     * 优先获得系统配置的ip，如果没有，那么取第一块 网卡的ip
     *
     * @return ip
     */
    public static String getIp() {
        String Ip = SystemConfig.getInstance().getString("swift.ip", "");
        if (StringUtils.isBlank(Ip) || !isIpAddressLegal(Ip)) {
            return getLocalIp();
        } else {
            return Ip;
        }
    }

    /**
     * 校验ip是否合法
     *
     * @param ip
     * @return true or false
     */
    private static boolean isIpAddressLegal(String ip) {
        Pattern pattern = Pattern.compile("(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])");
        Matcher matcher = pattern.matcher(ip);
        if (!matcher.matches()) {
            logger.error("your -Dswift.ip is not legal ,input ip is " + ip);
        }
        return matcher.matches();
    }

}
