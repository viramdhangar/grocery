/**
 * 
 */
package com.bestbuy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
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

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Viramm
 *
 */
@Entity
@Table(name="product")
@Data
@Setter
@Getter
@EqualsAndHashCode
public class ProductDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	@Column(length = 2000)
	private String specification;
	@Transient
	private Map<String, String> specificationMap;
	private String type;
	private String sellingPrize;
	private String mrp;
	private String size;
	private String availability;
	private String delivery;
	private String deliveryType;
	private int quantity;	
	private String weight;
	private int popularity;
	private String image;
	private int deliveryCharges;
	private int freeDelivery;
	private int paidDelivery;
	private String isCashOn;
	private int canCancel; //can cencel in number of days
	private String status; // active/discontinue
	private Long userId;
	private Date created;
	private Date updated;
	private boolean validProduct;
		
	@PrePersist
	protected void onCreate() {
		created = new Date();
	}
	
	@Transient
	private int selQuantity;
	@Transient
	private List<String> base64Data;
	@Transient
	private Long catId;
	@Transient
	private Long cartId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CategoryDTO category;
	
	@Transient
	private List<ImageModel> images;
	
	@Transient
	private List<ImageModel> imagesList;

}
