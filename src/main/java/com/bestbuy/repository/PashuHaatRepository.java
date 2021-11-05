package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bestbuy.model.PashuHaat;

public interface PashuHaatRepository extends JpaRepository<PashuHaat, Long> {

	public List<PashuHaat> findByType(String type); 
}
