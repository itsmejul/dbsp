package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="categories")
public class Categories {

	@Id
	@Column(name="id")
	private int id;
	@Column(name="title")
	private String title;
	@Column(name="parent_id")
	private int parentId;
	
	public Categories() {
		
	}
}
