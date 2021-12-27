package com.betfair.aping.entities;

import java.util.Date;

public class ItemDescription {

	private String eventTypeDesc;
	private String eventDesc;
	private String marketDesc;
	private String marketType;	
	private Date marketStartTime;
	private String runnerDesc;
	private int numberOfWinners;
	private double eachWayDivisor;
	
	
	public String getEventTypeDesc() {
		return eventTypeDesc;
	}
	public void setEventTypeDesc(String eventTypeDesc) {
		this.eventTypeDesc = eventTypeDesc;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getMarketDesc() {
		return marketDesc;
	}
	public void setMarketDesc(String marketDesc) {
		this.marketDesc = marketDesc;
	}
	public String getMarketType() {
		return marketType;
	}
	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}
	public Date getMarketStartTime() {
		return marketStartTime;
	}
	public void setMarketStartTime(Date marketStartTime) {
		this.marketStartTime = marketStartTime;
	}
	public String getRunnerDesc() {
		return runnerDesc;
	}
	public void setRunnerDesc(String runnerDesc) {
		this.runnerDesc = runnerDesc;
	}
	public int getNumberOfWinners() {
		return numberOfWinners;
	}
	public void setNumberOfWinners(int numberOfWinners) {
		this.numberOfWinners = numberOfWinners;
	}
	public double getEachWayDivisor() {
		return eachWayDivisor;
	}
	public void setEachWayDivisor(double eachWayDivisor) {
		this.eachWayDivisor = eachWayDivisor;
	}
}
