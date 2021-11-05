package com.bestbuy.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bestbuy.model.Order;
import com.bestbuy.model.ProductDTO;

public interface ProductRepository extends JpaRepository<ProductDTO, Long> {
	List<ProductDTO> findByCategoryId(Long categoryId);
	List<ProductDTO> findByUserIdOrderByCreatedDesc(Long userId);
	List<ProductDTO> findByNameIsContaining(String title);
	List<ProductDTO> findByTypeIsContaining(String type);
	@Modifying
	@Transactional
	@Query(value = "update product set quantity=? where id=?", nativeQuery = true)
	int updateProductQuantity(int quantity, Long id);
	
	@Query(value = "select * from product where category_id =? and valid_product=true limit ?,?", nativeQuery = true)
	List<ProductDTO> findByCategoryIdLimited( Long categoryId, int offset,  int limit);
	
	@Query(value = "select * from product where valid_product=true", nativeQuery = true)
	List<ProductDTO> findAllValid();
	
	
	@Query(value = "select * from product where user_id=? order by created desc limit ?,?", nativeQuery = true)
	public List<ProductDTO> findMyProductsLimited(Long userId, int offset,  int limit);
	
	@Query(value = "select * from product order by valid_product limit ?,?", nativeQuery = true)
	public List<ProductDTO> findMyProductsLimited(int offset,  int limit);
}
