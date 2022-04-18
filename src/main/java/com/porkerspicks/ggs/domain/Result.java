package com.porkerspicks.ggs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.porkerspicks.ggs.domain.types.RaceResult;

@Entity
public class Result {
	
	@Id
	private String betId;
	
	private int version;
	private double priceMatched;
	private double sizeMatched;
	private RaceResult raceResult;
	private double profit;
	private double commission;
	
	public String getBetId() {
		return betId;
	}
	public void setBetId(String betId) {
		this.betId = betId;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public double getPriceMatched() {
		return priceMatched;
	}
	public void setPriceMatched(double priceMatched) {
		this.priceMatched = priceMatched;
	}
	public double getSizeMatched() {
		return sizeMatched;
	}
	public void setSizeMatched(double sizeMatched) {
		this.sizeMatched = sizeMatched;
	}
	public RaceResult getRaceResult() {
		return raceResult;
	}
	public void setRaceResult(RaceResult raceResult) {
		this.raceResult = raceResult;
	}
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}
}