package com.bestbuy.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "Patrika")
public class Patrika {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Transient
	private List<PatrikaData> patrikaData;
	
	@Transient
	private List<PatrikaEvent> eventList;

	@Column(name = "vinit")
	private String vinit;
	
	@Column(name = "swagat")
	private String swagat;
	
	
	@Column(name = "contactPrimary")
	private String contactPrimary;
	
	
	@Column(name = "contactSecondary")
	private String contactSecondary;
	
	@Column(name = "reception")
	private String reception;
	
	@Column(name = "place")
	private String place;

	@Column(name = "bgCss")
	private String bgCss;
	/*
	 * @Column(name = "user") private String user;
	 */
	
	@Column(name = "pin")
	private String pin;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DAOUser user;
}
