package com.bestbuy.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="order_item")
public class OrderItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long productId;
	private String name;
	private String description;
	private String type;
	private String sellingPrize;
	private String mrp;
	private String size;
	private String availability;
	private String delivery;
	private String deliveryType;
	private int quantity;
	@Transient
	private int selQuantity;
	private String weight;
	private int popularity;
	private String image;
	private int deliveryCharges;
	private int freeDelivery;
	private int paidDelivery;
	private String isCashOn;
	private String cancelReason;
	private String refundStatus;
	private String comment;
	private int canCancel; //can cencel in number of days
	private String status;
	@Transient
	private String oldStatus;
	
	@Transient
	private List<String> base64Data;
	@Transient
	private Long catId;
	@Transient
	private Long cartId;
	private Date created;
	private Date updated;
	@Transient
	private List<TrackOrder> trackList;

	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Order order;
	
	@Transient
	private List<ImageModel> images;
	
	@PrePersist
	protected void onCreate() {
		created = new Date();
	}
}
