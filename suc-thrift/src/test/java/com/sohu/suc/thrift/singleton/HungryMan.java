package com.sohu.suc.thrift.singleton;

/**
 * 饿汉模式单例,就是不是懒加载,初始化耗内存.线程也安全,推荐这种模式.
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-26 12:18
 */
public class HungryMan {

    private HungryMan() {
    }

    private static HungryMan hungryMan = new HungryMan();

    public static HungryMan getInstance() {
        return hungryMan;
    }
}
