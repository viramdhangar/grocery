package com.bestbuy.repository;

import org.springframework.data.repository.CrudRepository;

import com.bestbuy.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long>{
	Iterable<Address> findByUserId(Long userId);
}
