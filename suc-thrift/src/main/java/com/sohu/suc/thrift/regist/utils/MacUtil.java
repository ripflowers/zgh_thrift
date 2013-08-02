package com.sohu.suc.thrift.regist.utils;


import org.apache.log4j.Logger;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * 获取当前机器的mac地址  获取第一块网卡的mac地址（仅仅适用于unix，linux系统）
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-17 17:40
 */
public final class MacUtil {

    private static final Logger logger = Logger.getLogger(MacUtil.class);

    private MacUtil() {
    }

    public static String getMacAddress() {
        String MacAddr = "";
        String str = "";
        try {
            NetworkInterface NIC = NetworkInterface.getByName("eth0");
            byte[] buf = NIC.getHardwareAddress();
            if (buf != null) {
                for (int i = 0; i < buf.length; i++) {
                    str = str + byteHEX(buf[i]);
                }
                MacAddr = str.toUpperCase();
            }
        } catch (SocketException e) {
            logger.error("MacUtil -> getMacAddress error !" + e.getMessage());
        }
        return MacAddr;
    }

    /* 一个将字节转化为十六进制ASSIC码的函数 */
    public static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

}
