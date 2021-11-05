package com.bestbuy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bestbuy.model.ImageModel;
import com.bestbuy.repository.ImageRepository;

@Service
public class ImageModelService implements IImageModelService{

	@Autowired
	ImageRepository imageRepository;
	
	@Override
	@Cacheable(value = "imageModelCache", key = "#productId")
    public List<ImageModel> getImages(Long productId) {
        System.out.println("Retrieving from Database for productId: " + productId);
        return imageRepository.findByProductId(productId);
    }
}
