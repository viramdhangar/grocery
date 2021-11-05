package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bestbuy.model.CartDTO;

@Repository
public interface CartRepository extends JpaRepository<CartDTO, Long>{
	public List<CartDTO> findByUserId(Long userId);
	void deleteByUserId(Long userId);
}

