package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bestbuy.model.ConfigDTO;

public interface ConfigRepository extends JpaRepository<ConfigDTO, Integer>{
	List<ConfigDTO> findAllByType(String type);
}
