package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bestbuy.model.PatrikaDistribution;

public interface PatrikaDistributionRepository extends CrudRepository<PatrikaDistribution, Long>{

	//@Query(value = "select * from patrika_distribution where state=? and district=? and tehsil=? and nagar=? and cast=? and shree like '%?%'", nativeQuery = true)
	public List<PatrikaDistribution> findByStateAndDistrictAndTehsilAndNagarAndCastAndShreeContaining(String state,String district,String tehsil,String nagar,String cast, String shree);
}
