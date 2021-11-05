package com.bestbuy.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bestbuy.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
	Optional<ImageModel> findByName(String name);
	List<ImageModel> findByProductId(Long productId);
	@Modifying
	@Transactional
	@Query(value = "delete from image_table where product_id=?", nativeQuery = true)
	int deleteByProductId(Long productId);
}
