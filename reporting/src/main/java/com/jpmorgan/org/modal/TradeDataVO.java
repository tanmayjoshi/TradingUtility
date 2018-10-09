package com.jpmorgan.org.modal;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TradeDataVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8645490971189133048L;
	
	private String entity;
	private Integer entityId;
	private String byeSellFlag;
	private double agreedFx;
	private String currency;
	private String instructionDate;
	private String settlementDate;
	@JsonIgnore
	private Date settlementDateTime;
	private Integer units;
	private double pricePerUnit;
	@JsonIgnore
	private double usdTradeAmt;
	@JsonIgnore
	private boolean isTradeSetteled;
	
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getByeSellFlag() {
		return byeSellFlag;
	}
	public void setByeSellFlag(String byeSellFlag) {
		this.byeSellFlag = byeSellFlag;
	}
	public double getAgreedFx() {
		return agreedFx;
	}
	public void setAgreedFx(double agreedFx) {
		this.agreedFx = agreedFx;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getInstructionDate() {
		return instructionDate;
	}
	public void setInstructionDate(String instructionDate) {
		this.instructionDate = instructionDate;
	}
	public String getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}
	public Integer getUnits() {
		return units;
	}
	public void setUnits(Integer units) {
		this.units = units;
	}
	public double getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	
	public double getUsdTradeAmt() {
		return usdTradeAmt;
	}
	public void setUsdTradeAmt(double usdTradeAmt) {
		this.usdTradeAmt = usdTradeAmt;
	} 
	
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	public boolean isTradeSetteled() {
		return isTradeSetteled;
	}
	public void setTradeSetteled(boolean isTradeSetteled) {
		this.isTradeSetteled = isTradeSetteled;
	}
	
	public Date getSettlementDateTime() {
		return settlementDateTime;
	}
	public void setSettlementDateTime(Date settlementDateTime) {
		this.settlementDateTime = settlementDateTime;
	}
	@Override
	public String toString() {
		return "TradeDataVO [entity=" + entity + ", entityId=" + entityId + ", byeSellFlag=" + byeSellFlag
				+ ", agreedFx=" + agreedFx + ", currency=" + currency + ", instructionDate=" + instructionDate
				+ ", settlementDate=" + settlementDate + ", settlementDateTime=" + settlementDateTime + ", units="
				+ units + ", pricePerUnit=" + pricePerUnit + ", usdTradeAmt=" + usdTradeAmt + ", isTradeSetteled="
				+ isTradeSetteled + "]";
	}
	
}
