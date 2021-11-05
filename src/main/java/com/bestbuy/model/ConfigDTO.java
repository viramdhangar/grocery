package com.bestbuy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
@Table(name="config")
public class ConfigDTO {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer rowId;
	private String type;
	private String id;
	private String header;
}
