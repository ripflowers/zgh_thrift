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
    对getConnection()的实现,就调用objectPool的borrow()方法,对returnCon()方法的实现,就调用objectPool的returnObject
    ()方法,对close()方法的实现,可以调用objectPool的clear()以及close()方法.
    然后定义一个PoolableObjectFactory,此factory为ConnectionPool 生产对象,当ConnectionPool为空的时候,有PoolableObject
    Factory为其生产对象,这可能有三种情形:
    1,当程序刚开始运行的时候,ConnectionPool为空,所以第一次调用的时候,需要PoolableObjectFactory来生产出Connection
    2,当很多线程来请求borrow object的时候,线程池中连接都被借走了,那么有PoolableObjectFactory来生产处新的连接.
    3,当connection们被borrow,但是没有被return(可能是借出的connection已经不可用了,所以没有被归还),那么再次需要使用的
    时候,有PoolableFactroy 来生产处新的可用连接.
    一个ConnectionFactory 需要实现PoolableObjectFactory 接口,这个接口需要以下五个方法来被实现:
    1,makeObject(): 顾名思义,就是生产处一个对象
    2,destroyObject(): 销毁一个对象.
    3,validateObject(): 验证一个对象是否可用.
    4,activateObject(): 激活一个对象
    5,passivateObject(): 钝化一个对象
    作为一个thrift连接池的实现,可以简单的实现前三个方法即可,makeObject()的时候,产生一个thrift连接,验证的时候调用
    isOpen()方法来验证连接是否可用,destroyObject() 中调用close()来销毁链接.
    如何使用这个连接呢,可以使用aop的思想,首先定义一个ConnectionManager,ConnectionManager中定义一个ThreadLocal来保存
    thrift连接,在客户端调用方法之前ConnectionManager根据之前定义好的ObjectPool以及PoolableObjectFactory来生产处连接,
    客户端调用的时候,从ConnectionManager的threadLocal中获得连接,方法调用之后,如果成功,那么ConnectionManager把连接返
    回给连接池,如果没有执行成功,则丢弃该连接(即不执行返回逻辑),最后从ThreadLocal中去除该连接.
    
    
