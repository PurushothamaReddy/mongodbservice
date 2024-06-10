package com.rest.mongo.mongodbservice.service;

import com.rest.mongo.mongodbservice.collection.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
    String addPhoto(String imageName, MultipartFile image) throws IOException;

    Photo getPhoto(String id);
}
