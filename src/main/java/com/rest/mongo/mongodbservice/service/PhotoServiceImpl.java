package com.rest.mongo.mongodbservice.service;

import com.rest.mongo.mongodbservice.collection.Photo;
import com.rest.mongo.mongodbservice.repository.PhotoRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PhotoServiceImpl implements PhotoService{
    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public String addPhoto(String imageName, MultipartFile image) throws IOException {
        Photo photo = Photo.builder().build();
        photo.setTitle(imageName);
        photo.setPhoto(new Binary(BsonBinarySubType.BINARY,image.getBytes()));

        return photoRepository.save(photo).getId();
    }

    @Override
    public Photo getPhoto(String id) {
        return photoRepository.findById(id).get();
    }
}
