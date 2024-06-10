package com.rest.mongo.mongodbservice.service;

import com.rest.mongo.mongodbservice.collection.Person;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface PersonService {

    String save(Person person);

    List<Person> getThePersonStartWith(String name);

    void deleteById(String id);

    List<Person> getThePersonByAge(Integer minAge, Integer maxAge);

    Page<Person> searchPerson(String name, Integer minAge, Integer maxAge, String city, Pageable pageable);

    List<Document> getOldestPersonInCityByAge();

    List<Document> getPopulationByCity();
}
