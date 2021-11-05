package com.bestbuy.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bestbuy.model.Authorities;
import com.bestbuy.model.DAOUser;
import com.bestbuy.repository.AuthoritiesRepository;
import com.bestbuy.repository.UserDao;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DAOUser user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	public DAOUser save(DAOUser user) {
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		userDao.save(user);
		Authorities authorities = new Authorities();
		authorities.setUsername(user.getUsername());
		authorities.setAuthority("ROLE_USER");
		authorities.setUser(user);
		authoritiesRepository.save(authorities);
		return userDao.findByUsername(user.getUsername());
	}
}