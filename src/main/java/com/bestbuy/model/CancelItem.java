package com.bestbuy.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CancelItem {

	private Order order;
	private OrderItem orderItem;
}
