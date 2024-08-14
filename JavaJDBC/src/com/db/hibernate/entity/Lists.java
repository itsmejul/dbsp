package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lists")
public class Lists {

	@Id
	@Column(name="asin")
	private String asin;
	@Id
	@Column(name="listname")
	private String listname;
	
	public Lists() {
		
	}
}
