package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="bookspec")
public class Bookspec {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="book_id")
	private Integer id;
	@Column(name="asin")
	private String asin;
	@Column(name="binding")
	private String binding;
	@Column(name="edition")
	private String edition;
	@Column(name="isbn")
	private String isbn;
	@Column(name="package_weight")
	private Integer weight;
	@Column(name="package_height")
	private Integer height;
	@Column(name="package_length")
	private Integer length;
	@Column(name="pages")
	private Integer pages;
	@Column(name="publication_date")
	private String date;
	
	public Bookspec() {
		
	}

	public Bookspec(String asin, String binding, String edition, String isbn, Integer weight, Integer height, Integer length,
			Integer pages, String date) {
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
