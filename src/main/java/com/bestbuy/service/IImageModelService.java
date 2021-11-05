package com.bestbuy.service;

import java.util.List;

import com.bestbuy.model.ImageModel;

public interface IImageModelService {

	List<ImageModel> getImages(Long productId);
}
