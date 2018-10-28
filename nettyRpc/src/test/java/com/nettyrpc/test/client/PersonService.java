package com.nettyrpc.test.client;

import java.util.List;

public interface PersonService {
    List<Person> getTestPerson(String name, int num);
}
