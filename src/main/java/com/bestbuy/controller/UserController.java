package com.bestbuy.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.Authorities;
import com.bestbuy.model.DAOUser;
import com.bestbuy.model.MailResponse;
import com.bestbuy.repository.AuthoritiesRepository;
import com.bestbuy.repository.UserRepository;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@GetMapping("/userById/{id}")
	public DAOUser userById(@PathVariable Long id) {
		
		DAOUser userDTO = new DAOUser();
		Optional<DAOUser> user = userRepository.findById(id);
		if(user.get() != null) {
			userDTO = user.get();
		}
		List<Authorities> authorities = authoritiesRepository.findAllById(userDTO.getId());
		userDTO.setAuthorities(authorities);
		return userDTO;
	}
	
	@GetMapping("/userByUsername/{username}")
	public DAOUser userByUsername(@PathVariable String username) {
		DAOUser user = userRepository.findByUsername(username);
		List<Authorities> auth =  authoritiesRepository.findByUserId(user.getId());
		user.setAuthorities(auth);
		return user;
	}
	
	@PostMapping("/resetPassword")
	public DAOUser resetPassword(@RequestBody DAOUser userDTO) {
		DAOUser userDBDTO = userRepository.findByEmail(userDTO.getEmail());
		userDBDTO.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
		return userRepository.save(userDBDTO);
	}
	
	@PostMapping("/changePassword")
	public MailResponse changePassword(@RequestBody DAOUser userDTO) {
		DAOUser userDBDTO = userRepository.findByEmail(userDTO.getEmail());
		if(bcryptEncoder.matches(userDTO.getPassword(), userDBDTO.getPassword())) {
			userDBDTO.setPassword(bcryptEncoder.encode(userDTO.getChangePassword()));
			userRepository.save(userDBDTO);
			return new MailResponse("Password updated successfully.", HttpStatus.OK);
		}else {
			return new MailResponse("Old and new password does not matching.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/*
	 * @PostMapping("/user") 
	 * public DAOUser saveUser(@Valid @RequestBody DAOUser
	 * user) { return userRepository.save(user); }
	 */

	/*
	 * @GetMapping("/posts")
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/posts/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }


    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
	 * 
	 */
  
}
