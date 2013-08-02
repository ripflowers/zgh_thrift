package com.sohu.suc.thrift.singleton;

/**
 * 懒汉模式的单例(线程不安全,但是基本可用)
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-26 12:08
 */
public class LazyMan {

    private LazyMan() {
    }

    private static LazyMan lazyMan = null;

    public static LazyMan getInstance() {
        if (lazyMan != null) {
            return lazyMan;
        } else {
            synchronized (LazyMan.class) {
                if (lazyMan != null) {
                    return lazyMan;
                }
                lazyMan = new LazyMan();
            }
            return lazyMan;
        }
    }

    public static void main(String... args) {

        // not thread safe indeed .. but even never shown . because out-of-order writes cost little time .
        LazyMan l = LazyMan.getInstance();
        LazyMan l2 = LazyMan.getInstance();
        System.out.println(l.equals(l2));       //true
        System.out.println(l == l2);            //true
    }

}
