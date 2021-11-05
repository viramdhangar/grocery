package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bestbuy.model.Patrika;
import com.bestbuy.model.PatrikaEvent;

public interface PatrikaEventRepository extends CrudRepository<PatrikaEvent, Long> {

	public List<PatrikaEvent> findAllByPatrika(Patrika p);
	public void deleteByPatrika(Patrika p);
}
