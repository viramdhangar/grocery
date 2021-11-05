package com.bestbuy.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.Address;
import com.bestbuy.model.DeliveryLocations;
import com.bestbuy.repository.AddressRepository;
import com.bestbuy.repository.DeliveryLocationsRepository;
import com.bestbuy.repository.UserRepository;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class AddressController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private DeliveryLocationsRepository locationsRepository;

	@GetMapping("/addressesByUser/{userId}")
	public Iterable<Address> addresses(@PathVariable Long userId) {
		return addressRepository.findByUserId(userId);
	}
	
	@GetMapping("/addressById/{id}")
	public Optional<Address> address(@PathVariable Long id) {
		return addressRepository.findById(id);
	}
	
	@PostMapping("/address/{userId}")
	public Optional<Object> saveAddress(@PathVariable (value = "userId") Long userId, @Valid @RequestBody Address address) {
		return userRepository.findById(userId).map(user -> {
			address.setUser(user);
            addressRepository.save(address);
            return addresses(userId);
        });
	}
	
	@GetMapping("/deliveryLocations/{pinCode}")
	public List<DeliveryLocations> deliveryLocations(@PathVariable String pinCode) {
		List<DeliveryLocations> pincode = locationsRepository.findByPinCodeStartsWith(pinCode);
		List<DeliveryLocations> location = locationsRepository.findByPostOfficeStartsWith(pinCode);
		pincode.addAll(location);
		return pincode;
	}
	
	/*
	 * @GetMapping("/posts/{postId}/comments") public Page<Comment>
	 * getAllCommentsByPostId(@PathVariable (value = "postId") Long postId, Pageable
	 * pageable) { return commentRepository.findByPostId(postId, pageable); }
	 * 
	 * @PostMapping("/posts/{postId}/comments") public Comment
	 * createComment(@PathVariable (value = "postId") Long postId,
	 * 
	 * @Valid @RequestBody Comment comment) { return
	 * postRepository.findById(postId).map(post -> { comment.setPost(post); return
	 * commentRepository.save(comment); }).orElseThrow(() -> new
	 * ResourceNotFoundException("PostId " + postId + " not found")); }
	 * 
	 * @PutMapping("/posts/{postId}/comments/{commentId}") public Comment
	 * updateComment(@PathVariable (value = "postId") Long postId,
	 * 
	 * @PathVariable (value = "commentId") Long commentId,
	 * 
	 * @Valid @RequestBody Comment commentRequest) {
	 * if(!postRepository.existsById(postId)) { throw new
	 * ResourceNotFoundException("PostId " + postId + " not found"); }
	 * 
	 * return commentRepository.findById(commentId).map(comment -> {
	 * comment.setText(commentRequest.getText()); return
	 * commentRepository.save(comment); }).orElseThrow(() -> new
	 * ResourceNotFoundException("CommentId " + commentId + "not found")); }
	 * 
	 * @DeleteMapping("/posts/{postId}/comments/{commentId}") public
	 * ResponseEntity<?> deleteComment(@PathVariable (value = "postId") Long postId,
	 * 
	 * @PathVariable (value = "commentId") Long commentId) { return
	 * commentRepository.findByIdAndPostId(commentId, postId).map(comment -> {
	 * commentRepository.delete(comment); return ResponseEntity.ok().build();
	 * }).orElseThrow(() -> new
	 * ResourceNotFoundException("Comment not found with id " + commentId +
	 * " and postId " + postId)); }
	 */

}
