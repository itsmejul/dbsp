package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="labels")
public class Labels {

	@Id
	@Column(name="label_name")
	private String name;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Labels() {
		
	}
}
