package com.bestbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bestbuy.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

	public List<OrderItem> findByOrderIdOrderByCreatedDesc(Long orderId);
	@Modifying
	@Transactional
	@Query(value = "update order_item set status=?, comment=?, cancel_reason=?, refund_status=?, updated=current_timestamp where id=?", nativeQuery = true)
	public int updateStatusAndComment(String status, String comment, String cancelReason, String refundStatus, Long orderId);
}
