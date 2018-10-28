package com.nettyrpc.test.server;

import com.nettyrpc.server.RpcService;
import com.nettyrpc.test.client.Person;
import com.nettyrpc.test.client.PersonService;

import java.util.ArrayList;
import java.util.List;
@RpcService(PersonService.class)
public class PersonServiceImpl implements PersonService {
    @Override
    public List<Person> getTestPerson(String name, int num) {
        List<Person> persons = new ArrayList<>();
        for (int i=0; i < num; ++i){
            persons.add(new Person(Integer.toString(i), name));
        }
        return persons;
    }
}
