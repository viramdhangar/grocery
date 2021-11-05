package com.bestbuy.service;

import java.util.List;

import com.bestbuy.model.CategoryDTO;

public interface ICategoryService {
	List<CategoryDTO> getCategories(String all);
}
