package com.bestbuy.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bestbuy.model.CategoryDTO;
import com.bestbuy.repository.CategoryRepository;
import com.bestbuy.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService{

	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	@Cacheable(value = "categoryCache", key = "#all")
	public List<CategoryDTO> getCategories(String all) {
		System.out.println("Retrieving from Database for all: " + all);
		List<CategoryDTO> catList = categoryRepository.findAll();
		catList.forEach(cat->{
			if(cat.getPicByte() != null) {
				cat.setPicByte(decompressBytes(cat.getPicByte()));
			}
		});
		return catList;
	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}
