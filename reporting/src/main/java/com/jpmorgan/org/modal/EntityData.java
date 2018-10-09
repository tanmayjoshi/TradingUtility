package com.jpmorgan.org.modal;

import java.io.Serializable;

public class EntityData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6798069848907453442L;

	private String entityName;
	private Integer entityId;
	private double buyingPrice;
	private double sellingPrice;
	
	public EntityData() {
		super();
	}

	public EntityData(String entityName, Integer entityId, double buyingPrice, double sellingPrice) {
		super();
		this.entityName = entityName;
		this.entityId = entityId;
		this.buyingPrice = buyingPrice;
		this.sellingPrice = sellingPrice;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public double getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	@Override
	public String toString() {
		return "EntityData [entityName=" + entityName + ", entityId=" + entityId + ", buyingPrice=" + buyingPrice
				+ ", sellingPrice=" + sellingPrice + "]";
	}
	
	
}
