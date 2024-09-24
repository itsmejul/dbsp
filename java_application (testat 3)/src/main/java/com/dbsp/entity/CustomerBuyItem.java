package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="customer_buy_item")
public class CustomerBuyItem {

	@Id
	@Column(name="customer_id")
	private Integer id;
	@Column(name="asin")
	private String asin;
	@Column(name="price_id")
	private Integer priceId;
	@Column(name="time_of_buy")
	private String timeOfBuy;
	
	public CustomerBuyItem() {
		
	}

	public CustomerBuyItem(Integer id, String asin, Integer priceId, String timeOfBuy) {
		this.id = id;
		this.asin = asin;
		this.priceId = priceId;
		this.timeOfBuy = timeOfBuy;
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

	public Integer getPriceId() {
		return priceId;
	}

	public void setPriceId(Integer priceId) {
		this.priceId = priceId;
	}

	public String getTimeOfBuy() {
		return timeOfBuy;
	}

	public void setTimeOfBuy(String timeOfBuy) {
		this.timeOfBuy = timeOfBuy;
	}
	
}
