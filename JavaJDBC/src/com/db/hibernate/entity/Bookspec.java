package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bookspec")
public class Bookspec {

	@Id
	@Column(name="book_id")
	private int id;
	@Column(name="asin")
	private String asin;
	@Column(name="binding")
	private String binding;
	@Column(name="edition")
	private String edition;
	@Column(name="isbn")
	private String isbn;
	@Column(name="package_weight")
	private int weight;
	@Column(name="package_height")
	private int height;
	@Column(name="package_length")
	private int length;
	@Column(name="pages")
	private int pages;
	@Column(name="publication_date")
	private String date;
	
	public Bookspec() {
		
	}
}
