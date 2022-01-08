package com.betfair.aping.entities;


import com.betfair.aping.enums.BetTargetType;
import com.betfair.aping.enums.PersistenceType;
import com.betfair.aping.enums.TimeInForce;

public class LimitOrder {

	private double size;
	private double price;
	private PersistenceType persistenceType;
	private TimeInForce timeInForce;
	private Double minFillSize;
	private BetTargetType betTargetType;
	private Double betTargetSize;


	public TimeInForce getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(TimeInForce timeInForce) {
		this.timeInForce = timeInForce;
	}

	public Double getMinFillSize() {
		return minFillSize;
	}

	public void setMinFillSize(double minFillSize) {
		this.minFillSize = minFillSize;
	}

	public BetTargetType getBetTargetType() {
		return betTargetType;
	}

	public void setBetTargetType(BetTargetType betTargetType) {
		this.betTargetType = betTargetType;
	}

	public Double getBetTargetSize() {
		return betTargetSize;
	}

	public void setBetTargetSize(double betTargetSize) {
		this.betTargetSize = betTargetSize;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public PersistenceType getPersistenceType() {
		return persistenceType;
	}

	public void setPersistenceType(PersistenceType persistenceType) {
		this.persistenceType = persistenceType;
	}

}
