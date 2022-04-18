package com.porkerspicks.ggs.domain;

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
	private double mbo = 0;
	private double uol = 0;
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
		startTime.setSeconds(0);
	}
	public Bet getPlacedBet() {
		return placedBet;
	}
	public void setPlacedBet(Bet placedBet) {
		this.placedBet = placedBet;
	}

	public double getUol() {
		return uol;
	}

	public void setUol(double uol) {
		this.uol = uol;
	}
}
