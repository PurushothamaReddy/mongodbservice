package com.rest.mongo.mongodbservice.service;

import com.rest.mongo.mongodbservice.collection.Person;
import com.rest.mongo.mongodbservice.repository.PersonRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String save(Person person) {
        return personRepository.save(person).getPersonId();
    }

    @Override
    public List<Person> getThePersonStartWith(String name) {
        return personRepository.findByFirstNameStartsWith(name);
    }

    @Override
    public void deleteById(String id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Person> getThePersonByAge(Integer minAge, Integer maxAge) {
        return personRepository.findByPersonAge(minAge, maxAge);
    }

    @Override
    public Page<Person> searchPerson(String name, Integer minAge, Integer maxAge, String city, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> list = new ArrayList<>();
        if (!StringUtils.isEmpty(name)) {
            list.add(Criteria.where("firstName").regex(name, "i"));
        }
        if (minAge != null && maxAge != null) {
            list.add(Criteria.where("age").gte(minAge).lt(maxAge));
        }
        if (!StringUtils.isEmpty(city)) {
            list.add(Criteria.where("addresses.city").is(city));
        }
        if (!list.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(list.toArray(new Criteria[0])));
        }
        // to exclude addresses in response
        query.fields().exclude("addresses");

        Page<Person> people = PageableExecutionUtils.getPage(mongoTemplate.find(query, Person.class), pageable, () -> mongoTemplate.count(query.skip(0).limit(0), Person.class));
        return people;
    }

    @Override
    public List<Document> getOldestPersonInCityByAge() {

        UnwindOperation unwindOperation = Aggregation.unwind("addresses");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC,
                "age");
        //sorting is done in the reverse order of age take the first document after group by
        GroupOperation groupOperation = Aggregation.group("addresses.city").first(Aggregation.ROOT).as("oldestPerson");
        Aggregation aggregation = Aggregation.newAggregation(unwindOperation, sortOperation, groupOperation);
        List<Document> persons = mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
        return persons;
    }

    @Override
    public List<Document> getPopulationByCity() {
        UnwindOperation unwindOperation = Aggregation.unwind("addresses");
        GroupOperation groupOperation = Aggregation.group("addresses.city").count().as("popCount");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC,"popCount");
        ProjectionOperation projectionOperation = Aggregation.project().andExpression("_id").as("city")
                .andExpression("popCount").as("count")
                .andExclude("_id");
        Aggregation aggregation = Aggregation.newAggregation(unwindOperation,groupOperation,sortOperation,projectionOperation);

        List<Document> documents = mongoTemplate.aggregate(aggregation,Person.class,Document.class).getMappedResults();

        return documents;
    }

}
