package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bestbuy.model.Authorities;

public interface AuthoritiesRepository extends CrudRepository<Authorities, Long> {

	public List<Authorities> findAllById(Long id);
	public List<Authorities> findByUserId(Long userId);

}
