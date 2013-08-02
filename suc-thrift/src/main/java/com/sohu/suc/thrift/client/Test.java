package com.sohu.suc.thrift.client;

import com.sohu.suc.thrift.gen.Person;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * @author: guohaozhao (guohaozhao116008@sohu-inc.com)
 * @since: 13-6-19 17:58
 */
public class Test {

    public static void main(String... args) throws TException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        final PersonThriftClientWithPool client = (PersonThriftClientWithPool) applicationContext.getBean("zghTestClient");

        final CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Person person = new Person();
                    person.setName("guohaozhao");
                    person.setAge(21);
                    try {
                        System.out.println(client.personToString(person));
                    } catch (TException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    countDownLatch.countDown();
                }
            });

            thread.start();

        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("done");
        client.close();
        System.exit(0);


    }

}
