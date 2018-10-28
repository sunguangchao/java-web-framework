package com.nettyrpc.test.app;

import com.nettyrpc.client.AsyncRPCCallback;
import com.nettyrpc.client.RPCFuture;
import com.nettyrpc.client.RpcClient;
import com.nettyrpc.client.proxy.IAsyncObjectProxy;
import com.nettyrpc.registry.ServiceDiscovery;
import com.nettyrpc.test.client.Person;
import com.nettyrpc.test.client.PersonService;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PersonCallbackTest {
    public static void main(String[] args) {
        //根据地址，连接zk
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("");
        final RpcClient rpcClient = new RpcClient(serviceDiscovery);
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            IAsyncObjectProxy client = RpcClient.createAsync(PersonService.class);
            int num = 5;
            RPCFuture helloPersonFuture = client.call("GetTestPerson", "xiaoming", num);
            helloPersonFuture.addCallback(new AsyncRPCCallback() {
                @Override
                public void success(Object result) {
                    List<Person> persons = (List<Person>)result;
                    for (int i = 0; i < persons.size(); ++i){
                        System.out.println(persons.get(i));
                    }
                    countDownLatch.countDown();
                }

                @Override
                public void fail(Exception e) {
                    System.out.println(e);
                    countDownLatch.countDown();

                }
            });

        }catch (Exception e){
            System.out.println(e);
        }

        try {
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        rpcClient.stop();
        System.out.println("End");
    }
}
