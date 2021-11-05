package com.bestbuy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
@Table(name = "user")
public class DAOUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(unique = true)
	private String username;
	private String email;
	private String password;
	private String phone;
	private String firstName;
	private String lastName;
	private String pinCode;
	private Date created;
	private String changePassword;
	@Transient
	private List<Authorities> authorities;

	/*
	 * @OneToMany(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "user_username", referencedColumnName = "username")
	 * // @Fetch(value = FetchMode.SUBSELECT) private List<Address> addresses = new
	 * ArrayList<>();
	 */
	 

	/*
	 * @OneToMany(targetEntity = Order.class, fetch = FetchType.EAGER)
	 * 
	 * @Fetch(value = FetchMode.SUBSELECT) private List<Order> orders;
	 */

	@PrePersist
	protected void onCreate() {
		created = new Date();
	}

}