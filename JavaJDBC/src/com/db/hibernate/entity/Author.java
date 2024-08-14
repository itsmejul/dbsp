package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="author")
public class Author {

	@Id
	@Column(name="author_name")
	private String name;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Author() {
		
	}
}
