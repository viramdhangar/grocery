package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bestbuy.model.PashuImages;

public interface PashuImagesRepository extends JpaRepository<PashuImages, Long>{
	List<PashuImages> findByPashuHaatId(Long pashuHaatId);
}
