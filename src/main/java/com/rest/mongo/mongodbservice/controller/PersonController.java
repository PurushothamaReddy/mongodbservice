package com.rest.mongo.mongodbservice.controller;

import com.rest.mongo.mongodbservice.collection.Person;
import com.rest.mongo.mongodbservice.service.PersonService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping
    public String save(@RequestBody Person person) {

        return personService.save(person);

    }

    @GetMapping
    public List<Person> getThePersonStartWith(@RequestParam("name") String name) {
        return personService.getThePersonStartWith(name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        personService.deleteById(id);
    }

    @GetMapping("/age")
    public List<Person> getThePersonByAge(@RequestParam("minAge") Integer minAge, @RequestParam("maxAge") Integer maxAge) {
        return personService.getThePersonByAge(minAge, maxAge);
    }

    @GetMapping("/search")
    public Page<Person> searchPerson(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) Integer minAge,
                                     @RequestParam(required = false) Integer maxAge,
                                     @RequestParam(required = false) String city,
                                     @RequestParam(defaultValue = "0") Integer offset,
                                     @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(offset, size);
        return personService.searchPerson(name, minAge, maxAge, city, pageable);
    }


    @GetMapping("/oldestPerson")
    public List<Document> getOldestPersonInCityByAge(){
        return personService.getOldestPersonInCityByAge();
    }
    @GetMapping("/populationByCity")
    public List<Document> getPopulationByCity() {
        return personService.getPopulationByCity();
    }

}
