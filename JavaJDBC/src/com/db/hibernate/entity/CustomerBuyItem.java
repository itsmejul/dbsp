package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customer_buy_item")
public class CustomerBuyItem {

	@Id
	@Column(name="customer_id")
	private int id;
	@Column(name="asin")
	private String asin;
	@Column(name="price_id")
	private int priceId;
	@Column(name="time_of_buy")
	private String timeOfBuy;
	
	public CustomerBuyItem() {
		
	}

	public CustomerBuyItem(int id, String asin, int priceId, String timeOfBuy) {
		this.id = id;
		this.asin = asin;
		this.priceId = priceId;
		this.timeOfBuy = timeOfBuy;
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

	public int getPriceId() {
		return priceId;
	}

	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}

	public String getTimeOfBuy() {
		return timeOfBuy;
	}

	public void setTimeOfBuy(String timeOfBuy) {
		this.timeOfBuy = timeOfBuy;
	}
	
}
