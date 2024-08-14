package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="actor")
public class Actor {

	@Id
	@Column(name="actor_name")
	private String name;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Actor() {
		
	}
}
