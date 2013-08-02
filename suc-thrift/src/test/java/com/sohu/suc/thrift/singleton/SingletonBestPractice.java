package com.sohu.suc.thrift.singleton;

/**
 * 单例模式的最佳实践
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-26 12:20
 */
public class SingletonBestPractice {

    private SingletonBestPractice() {
    }

    private static class SingletionHolder {
        private static SingletonBestPractice singleton = new SingletonBestPractice();
    }

    public static SingletonBestPractice getInstances() {
        return SingletionHolder.singleton;
    }


}
