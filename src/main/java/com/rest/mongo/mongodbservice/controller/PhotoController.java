package com.rest.mongo.mongodbservice.controller;

import com.rest.mongo.mongodbservice.collection.Photo;
import com.rest.mongo.mongodbservice.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping
    public String addPhoto(@RequestParam("image") MultipartFile image) throws IOException {
        String imageName = image.getOriginalFilename();
        return photoService.addPhoto(imageName, image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String id) {
        Photo photo = photoService.getPhoto(id);

        Resource resource = new ByteArrayResource(photo.getPhoto().getData());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }
}
