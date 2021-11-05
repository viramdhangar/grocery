package com.bestbuy.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.CartDTO;
import com.bestbuy.model.ProductDTO;
import com.bestbuy.service.impl.CartService;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class CartController {

	@Autowired
	private CartService cartService;
	
	@GetMapping("/cartByUser/{userId}")
	public List<ProductDTO> cartByUser(@PathVariable Long userId) {
		return cartService.cartByUser(userId);
	}
	
	@PostMapping("/addToCart/{userId}")
	public CartDTO addToCart(@PathVariable (value = "userId") Long userId, @Valid @RequestBody ProductDTO product) {
		return cartService.addToCart(product, userId);
	}
	
	@DeleteMapping("/removeFromCart/{cartId}")
	public Long addToCart(@PathVariable (value = "cartId") Long cartId) {
		cartService.deleteByCartId(cartId);
		return cartId;
	}
	
	@GetMapping("/cartCount/{userId}")
	public List<CartDTO> getCartCount(@PathVariable Long userId) {
		return cartService.getCartCount(userId);
	}
	
	@DeleteMapping("/deleteCart/{userId}")
	public Long delereCart(@PathVariable (value = "userId") Long userId) {
		cartService.deleteByUserId(userId);
		return userId;
	}
}
