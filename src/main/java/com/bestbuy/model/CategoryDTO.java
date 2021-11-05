/**
 * 
 */
package com.bestbuy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.Setter;

/**
 * @author Viramm
 *
 */
@Entity
@Table(name="category")
@Data
@Setter
public class CategoryDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String isActive;
	@Column(name = "type")
	private String type;
	@Lob
	@Column(name = "picByte", length=100000)
	private byte[] picByte;
	
	/*
	 * @OneToMany(targetEntity=ProductDTO.class, fetch = FetchType.EAGER) private
	 * List<ProductDTO> products;
	 */
	
}
