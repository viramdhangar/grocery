package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bestbuy.model.Order;
import com.bestbuy.model.ProductDTO;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	public List<Order> findByUserIdOrderByCreatedDesc(Long userId);
	@Query(value = "update orders set status=? where id=?", nativeQuery = true)
	public int updateStatus(String status, Long id);
	@Query(value = "select * from orders where status=? order by id desc", nativeQuery = true)
	public List<Order> findAllOrdered(String status);
	
	@Query(value = "select * from orders order by id desc limit ?,?", nativeQuery = true)
	public List<Order> findAllOrderedLimited(int offset,  int limit);
	
	@Query(value = "select * from orders where user_id=? order by id desc limit ?,?", nativeQuery = true)
	public List<Order> findAllOrderedLimited(Long userId, int offset,  int limit);
	
	@Query(value = "select * from orders where user_id =? order by id desc limit ?,?", nativeQuery = true)
	public List<Order> findByUserIdOrderByCreatedDescLimited( Long userId, int offset,  int limit);
}
