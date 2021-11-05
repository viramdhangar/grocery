package com.bestbuy.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.Deflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bestbuy.model.CategoryDTO;
import com.bestbuy.repository.CategoryRepository;
import com.bestbuy.service.ICategoryService;

@RestController
@CrossOrigin (origins = {"*"}, maxAge = 3600)
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("category")
	public List<CategoryDTO> category () {
		String all  = "ALL";
		List<CategoryDTO> catList = categoryService.getCategories(all);		
		return catList;
	}
	
	@PostMapping("/catImage/{catId}")
	public CategoryDTO catImage (@PathVariable Long catId, @RequestParam("file") MultipartFile file) throws IOException {
		Optional<CategoryDTO> catDTO =  categoryRepository.findById(catId);
				
		catDTO.get().setType(file.getContentType());
		catDTO.get().setPicByte(compressBytes(file.getBytes()));
		categoryRepository.save(catDTO.get());
		return catDTO.get();
	}
	
	// compress the image bytes before storing it in the database
		public static byte[] compressBytes(byte[] data) {
			Deflater deflater = new Deflater();
			deflater.setInput(data);
			deflater.finish();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
			byte[] buffer = new byte[1024];
			while (!deflater.finished()) {
				int count = deflater.deflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			try {
				outputStream.close();
			} catch (IOException e) {
			}
			System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

			return outputStream.toByteArray();
		}
}
