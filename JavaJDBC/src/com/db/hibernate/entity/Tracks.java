package com.db.hibernate.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tracks")
public class Tracks {

	@Id
	@Column(name="track_title")
	private String title;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Tracks() {
		
	}

	public Tracks(String title, String asin) {
		super();
		this.title = title;
		this.asin = asin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}
	
}
