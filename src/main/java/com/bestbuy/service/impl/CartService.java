package com.bestbuy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestbuy.model.CartDTO;
import com.bestbuy.model.DAOUser;
import com.bestbuy.model.ProductDTO;
import com.bestbuy.repository.CartRepository;
import com.bestbuy.repository.ProductRepository;
import com.bestbuy.repository.UserRepository;
import com.bestbuy.service.ICartService;

@Service
public class CartService implements ICartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public List<ProductDTO> cartByUser(Long userId) {
		List<CartDTO> cartList = cartRepository.findByUserId(userId);
		List<ProductDTO> productList = new ArrayList<>();
		for (CartDTO cart : cartList) {
			Optional<ProductDTO> products  =  productRepository.findById(cart.getProductId());
			ProductDTO product = new ProductDTO();
			if(products.isPresent()) {
				product = products.get();
			}
			if (products.isPresent()) {
				product = products.get();
				product.setSelQuantity(cart.getSelQuantity());
				product.setCartId(cart.getId());
				productList.add(product);
			}
		}
		return productList;
	}

	@Override
	public CartDTO addToCart(ProductDTO product, Long userId) {
		// cartRepository.deleteByUserId(userId);
		Optional<DAOUser> user = userRepository.findById(userId);
		CartDTO cartDTO = new CartDTO();
		cartDTO.setProductId(product.getId());
		cartDTO.setId(product.getCartId());
		cartDTO.setSelQuantity(product.getSelQuantity());
		cartDTO.setUser(user.get());
		return cartRepository.save(cartDTO);
	}

	@Override
	public void deleteByCartId(Long cartId) {
		cartRepository.deleteById(cartId);
	}
	
	@Override
	public void deleteByUserId(Long userId) {
		List<CartDTO> cartList = cartRepository.findByUserId(userId);
		cartList.forEach(cart->{
			cartRepository.deleteById(cart.getId());	
		});
	}
	
	@Override
	public List<CartDTO> getCartCount(Long userId) {
		List<CartDTO> cartList = cartRepository.findByUserId(userId);
		return cartList;
	}

}
