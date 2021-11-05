package com.bestbuy.service;

import java.util.List;

import com.bestbuy.model.CartDTO;
import com.bestbuy.model.ProductDTO;

public interface ICartService {

	List<ProductDTO> cartByUser(Long userId);
	CartDTO addToCart(ProductDTO productList, Long userId);
	void deleteByCartId(Long cartId);
	List<CartDTO> getCartCount(Long userId);
	void deleteByUserId(Long userId);
}
