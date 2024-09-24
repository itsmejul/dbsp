package com.dbsp.entity;

import com.dbsp.entity.keys.ArtistId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "artist")
@IdClass(ArtistId.class)
public class Artist {

	@Id
	@Column(name = "artist_name")
	private String name;
	@Id
	@Column(name = "asin")
	private String asin;

	public Artist() {

	}

	public Artist(String name, String asin) {
		this.name = name;
		this.asin = asin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

}
