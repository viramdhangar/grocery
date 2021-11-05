package com.bestbuy.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	private int orderQuantity; // number of items
	private String shippingCharge; // total purchase order
	private String cartAmount; // total purchase order
	private String totalAmount; // total purchase order
	private String paymentMode; // cash/card/netbanking
	private Long deliveryAddressId; // delivery address
	private Long billingAddressId; // delivery address
	private Date created; // order placed date
	private Date deliveredOn; // order delivery date
	private String deliveryChannel; // by which channel.. courier
	private String deliveryBoy; // delivery boy
	private String status; // open/packed/shipped/delivered/closed
	private int deliveryCharges;
	private int deliveryChargesDiscount;
	private String validStatus;
	private String paymentId;
	private String refund;
	private String comment;
	private String cancelReason;
	private String refundStatus;
	//@JsonIgnore
	@Transient
	private List<ProductDTO> products;
	@Transient
	private OrderItem orderItem;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DAOUser user;
		
	@PrePersist
	protected void onCreate() {
		created = new Date();
	}
	@Transient
	private Address deliveryAddress;
	@Transient
	private Address billingAddress;
}
