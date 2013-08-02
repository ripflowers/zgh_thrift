package com.sohu.suc.thrift.regist.utils;

import org.apache.log4j.Logger;

/**
 * 获取zk 注册路径的工具类
 * 根路径区分测试环境和线上环境，通过Java系统变量来区分。
 * 环境 	       值 	    说明
 * 开发环境 	/talent 	无需配置
 * 线上环境 	/ 	        配置方法: -Dconfig.product=true
 * 测试环境 	/testing 	配置方法: -Dconfig.testing=true
 * <p/>
 * 我们的配置中心保存有线上环境、开发环境、测试环境三套配置，默认使用开发环境，为了使用线上环境使用如下其中一种方法需要做如下处理：
 * <p/>
 * Java进程增加参数（推荐）： -Dconfig.product=true；如果是resin 3.1可以在httpd.sh中增加。
 * 使用配置文件：suc-core.properties文件位于classpath中，包含内容 config.product=true
 * 手动设置（测试时推荐）:System.setProperty("config.product", "true");
 * <p/>
 * 通常情况下使用第一种方式是最有效也最可靠的方式。
 *
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-17 16:31
 */
public final class ZkPathUtil {

    private ZkPathUtil() {
    }

    private static final Logger logger = Logger.getLogger(ZkPathUtil.class);

    public static final String swiftZkConnect = "zk1.in.i.sohu.com:2181,zk2.in.i.sohu.com:2181,zk3.in.i.sohu.com:2181,zk4.in.i.sohu.com:2181,zk5.in.i.sohu.com:2181";

    //开发环境zk地址
    private static final String defaultRootPath = "/talent/suc/swift";
    //测试环境zk地址
    private static final String defaultTestingRootPath = "/testing/suc/swift";
    //线上环境zk地址
    private static final String defaultOnlineRootPath = "/suc/swift";

    /**
     * 获取默认的配置中心根路径
     * <p>
     * 查找顺序（Java系统属性以及配置文件）:
     * <ol>
     * <li>zookeeper.rootpath</li>
     * <li>config.product</li>
     * <li>config.testing</li>
     * </ol>
     * </p>
     *
     * @return 配置中心根路径
     */
    public static String getSwiftRegistryRootPath() {
        SystemConfig sc = SystemConfig.getInstance();
        if (sc.getBoolean("config.product", false)) {
            return defaultOnlineRootPath;
        }
        if (sc.getBoolean("config.testing", false)) {
            return defaultTestingRootPath;
        }
        return defaultRootPath;
    }
}
