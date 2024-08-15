package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

	public Bookspec(String asin, String binding, String edition, String isbn, int weight, int height, int length,
			int pages, String date) {
		this.asin = asin;
		this.binding = binding;
		this.edition = edition;
		this.isbn = isbn;
		this.weight = weight;
		this.height = height;
		this.length = length;
		this.pages = pages;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
