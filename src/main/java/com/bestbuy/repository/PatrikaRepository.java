package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bestbuy.model.DAOUser;
import com.bestbuy.model.Patrika;

public interface PatrikaRepository extends CrudRepository<Patrika, Long>{
	public List<Patrika> findAllByUser(DAOUser user);
}
