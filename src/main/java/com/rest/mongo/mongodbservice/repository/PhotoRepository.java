package com.rest.mongo.mongodbservice.repository;

import com.rest.mongo.mongodbservice.collection.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends MongoRepository<Photo,String> {
}
