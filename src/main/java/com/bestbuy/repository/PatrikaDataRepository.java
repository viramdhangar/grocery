package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bestbuy.model.Patrika;
import com.bestbuy.model.PatrikaData;

public interface PatrikaDataRepository extends CrudRepository<PatrikaData, Long> {

	public List<PatrikaData> findAllByPatrika(Patrika p);
	
	public void deleteByPatrika(Patrika p);
}
