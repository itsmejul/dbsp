package com.dbsp.entity;

import com.dbsp.entity.keys.TracksId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "tracks")
@IdClass(TracksId.class)
public class Tracks {

	@Id
	@Column(name = "track_title")
	private String title;
	@Id
	@Column(name = "asin")
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
