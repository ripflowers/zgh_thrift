Thrift 
============

### thrift 连接池的设计原理

#### thrift的java客户端并没有设计连接池,但是swift为thrift提供了一个连接池,但是swift是jdk 1.7编写的,对于jdk 1.6 编写的程序是不能够调用的,所以,我在这里编写了一个基于jdk1.6 的thrift连接池.

    主要用到的技术:
    1.apache ObjectPool
    2.apache ConnectionPool
    3.apache PoolableObjectFactory
    
#### 设计思想:

    首先,定义连接池接口,接口定义三个方法,获取一个连接,返回一个连接以及关闭连接池.
    public interface ConnectionPool {
        /**
         * 取链接池中的一个链接
         *
         * @return
         */
        public TFramedTransport getConnection();    
        /**
         * 返回链接
         *
         * @param tFramedTransport
         */
        public void returnCon(TFramedTransport tFramedTransport);    
        /**
         * 不可以轻易调用
         */
        public void close();
    }
    然后,对此接口做实现:
    实现类中 首先定义一个 private ObjectPool objectPool = null;
    然后再 类的构造时,对其进行初始化,可以实现在afterPropertiesSet 中.
    对getConnection()的实现,就调用objectPool的borrow()方法,对returnCon()方法的实现,就调用objectPool的returnObject()方法,对close()方法的实现,可以调用objectPool的clear()以及close()方法.
    
    
    
