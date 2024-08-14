package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="publishers")
public class Publishers {

	@Id
	@Column(name="publisher_name")
	private String name;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Publishers() {
		
	}
}
