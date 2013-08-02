package com.sohu.suc.thrift;

import org.apache.thrift.server.TThreadedSelectorServer;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-25 17:03
 */
public class ServerLog {

    public static void main(String ... args){
        System.out.println(TThreadedSelectorServer.class.getName());
    }

}
