package com.sohu.suc.thrift.regist;

/**
 * swiftServer的注册模块
 * server启动的时候调用 publish 方法 ，把自己发布到zk的目录中
 * 通过获取 -Dconfig.product=true 说明是线上server，注册到/suc/swift目录下
 * 通过获取 -Dconfig.testing=true 说明是测试server，注册到/testing/suc/swift目录下
 * 如果这两个参数都没有，说明是开发环境server，注册到/talent/suc/swift目录下
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-17 14:55
 */
public interface ThriftServiceRegister {

    public void publish();

}
