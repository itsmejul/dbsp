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

	public Categories(String title, int parentId) {
		this.title = title;
		this.parentId = parentId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
}
