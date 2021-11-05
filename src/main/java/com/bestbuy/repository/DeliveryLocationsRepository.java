package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bestbuy.model.DeliveryLocations;

@Repository
public interface DeliveryLocationsRepository extends CrudRepository<DeliveryLocations, Long>{
	public List<DeliveryLocations> findByPinCodeStartsWith(String pinCode);
	public List<DeliveryLocations> findByPostOfficeStartsWith(String location);
}
