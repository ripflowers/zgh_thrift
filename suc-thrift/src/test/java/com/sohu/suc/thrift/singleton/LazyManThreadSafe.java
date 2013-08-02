package com.sohu.suc.thrift.singleton;

/**
 * 一个线程安全的懒汉模式单例
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-26 12:14
 */
public class LazyManThreadSafe {

    private LazyManThreadSafe() {
    }

    private static LazyManThreadSafe lazyManThreadSafe = null;

    public static LazyManThreadSafe getInstance() {
        if (lazyManThreadSafe != null) {
            return lazyManThreadSafe;
        } else {
            synchronized (LazyManThreadSafe.class) {
                if (lazyManThreadSafe != null) {
                    return lazyManThreadSafe;
                } else {
                    LazyManThreadSafe temp = new LazyManThreadSafe();
                    lazyManThreadSafe = temp;
                }
                return lazyManThreadSafe;
            }
        }
    }

}
