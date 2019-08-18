package com.nettyrpc.test.server;

import com.nettyrpc.server.RpcService;
import com.nettyrpc.test.client.HelloService;
import com.nettyrpc.test.client.Person;
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    public HelloServiceImpl(){

    }

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}