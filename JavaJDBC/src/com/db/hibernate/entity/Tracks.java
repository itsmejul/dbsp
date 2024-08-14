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
}
