package com.sohu.suc.thrift.regist.utils;

import com.google.common.base.Function;
import com.sohu.suc.thrift.gen.SucThriftServer;
import org.apache.log4j.Logger;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

/**
 * ThriftServer 与 二进制数组之间的转换
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-18 10:25
 */
public class ThriftServerFunctionUtil {

    private static final Logger logger = Logger.getLogger(ThriftServerFunctionUtil.class);

    /**
     * ThriftServer -> byte[] 的转换函数
     */
    public static class ThriftServerToByteArray implements Function<SucThriftServer, byte[]> {
        @Override
        public byte[] apply(SucThriftServer input) {
            if (input == null) {
                // do not return null ,null sucks .
                return new SucThriftServer();
            }
            TSerializer tSerializer = new TSerializer(new TBinaryProtocol.Factory());
            try {
                byte[] bytes = tSerializer.serialize(input);
                return bytes;
            } catch (TException e) {
                logger.error("tSerializer thrift server error !!" + e.toString());
                return new SucThriftServer();
            }
        }
    }

    /**
     * String -> byte [] translate
     */
    public static class StringToByteArray implements Function<String, byte[]> {

        @Override
        public byte[] apply(java.lang.String s) {
            return s.getBytes();
        }
    }

    /**
     * byte[] -> ThriftServer 的转换函数
     */
    public static class ByteArrayToThriftServer implements Function<byte[], SucThriftServer> {
        @Override
        public SucThriftServer apply(byte[] input) {
            if (input == null) {
                return null;
            }
            TDeserializer tDeserializer = new TDeserializer(new TBinaryProtocol.Factory());
            SucThriftServer thriftServer = new SucThriftServer();
            try {
                tDeserializer.deserialize(thriftServer, input);
                return thriftServer;
            } catch (TException e) {
                logger.error("TDeserializer thrift server error !!" + e.toString());
                return null;
            }
        }
    }

    /**
     * byte[] -> String 的转换函数
     */
    public static class ByteArrayToString implements Function<byte[], String> {

        @Override
        public String apply(byte[] bytes) {
            return new String(bytes);
        }
    }

}
