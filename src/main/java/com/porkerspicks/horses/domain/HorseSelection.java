package com.porkerspicks.horses.domain;

import java.util.Date;

/**
 * 
 * @author wullp
 *
 * Holds the identifiers for a horse bet selection.
 *
 */
public class HorseSelection {
	
	private String selection;
	private double mbo;
	private Date startTime = new Date();
	private Bet placedBet;
	
	public String getSelection() {
		return selection;
	}
	public void setSelection(String selection) {
		this.selection = selection;
	}
	public double getMbo() {
		return mbo;
	}
	public void setMbo(double mbo) {
		this.mbo = mbo;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setStartTime( int hour, int minute ) {
		startTime.setHours(hour);
		startTime.setMinutes(minute);
	}
	public Bet getPlacedBet() {
		return placedBet;
	}
	public void setPlacedBet(Bet placedBet) {
		this.placedBet = placedBet;
	}
}
