package com.bestbuy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bestbuy.model.CategoryDTO;

public interface CategoryRepository  extends JpaRepository<CategoryDTO, Long> {

}
