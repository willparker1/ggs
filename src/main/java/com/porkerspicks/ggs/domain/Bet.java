package com.porkerspicks.ggs.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.betfair.aping.enums.InstructionReportErrorCode;
import com.betfair.aping.enums.InstructionReportStatus;

@Entity
public class Bet {
	
	@Id
	private String betId;
	
	private int version;
	private Date timestamp;
	private String selection;
	@Column(nullable = true)
	private double mbo;

	@Column(nullable = true)
	private double uol;
	private double averagePriceMatched;
	
	@Column(nullable = true)
	private double size;
	private double sizeMatched;
	private InstructionReportStatus status;
	private InstructionReportErrorCode errorCode;
	
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
	public double getUol() { return uol;}
	public void setUol(double uol) { this.uol = uol; }
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public double getAveragePriceMatched() {
		return averagePriceMatched;
	}
	public void setAveragePriceMatched(double averagePriceMatched) {
		this.averagePriceMatched = averagePriceMatched;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public double getSizeMatched() {
		return sizeMatched;
	}
	public void setSizeMatched(double sizeMatched) {
		this.sizeMatched = sizeMatched;
	}
	public InstructionReportStatus getStatus() {
		return status;
	}
	public void setStatus(InstructionReportStatus status) {
		this.status = status;
	}
	public InstructionReportErrorCode getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(InstructionReportErrorCode errorCode) {
		this.errorCode = errorCode;
	}	
}