package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="musicspec")
public class Musicspec {

	@Id
	@Column(name="cd_id")
	private Integer id;
	@Column(name="asin")
	private String asin;
	@Column(name="binding")
	private String binding;
	@Column(name="format")
	private String format;
	@Column(name="num_discs")
	private Integer num_discs;
	@Column(name="releasedate")
	private String releasedate;
	@Column(name="upc")
	private String upc;
	
	public Musicspec() {
		
	}

	public Musicspec(String asin, String binding, String format, Integer num_discs, String releasedate, String upc) {
		this.asin = asin;
		this.binding = binding;
		this.format = format;
		this.num_discs = num_discs;
		this.releasedate = releasedate;
		this.upc = upc;
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getNum_discs() {
		return num_discs;
	}

	public void setNum_discs(Integer num_discs) {
		this.num_discs = num_discs;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}
	
}
