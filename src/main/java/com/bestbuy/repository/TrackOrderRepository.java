package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bestbuy.model.TrackOrder;

@Repository
public interface TrackOrderRepository extends JpaRepository<TrackOrder, Long>{
	public List<TrackOrder> findByOrderItemId(Long orderItemId);
	@Modifying
	@Transactional
	@Query(value = "delete from track_order where order_item_id=? and status=?", nativeQuery = true)
	int deleteByOrderItemId(Long orderItemId, String status);
}
