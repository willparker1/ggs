package com.porkerspicks.ggs.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.betfair.aping.enums.BetStatus;
import com.betfair.aping.enums.InstructionReportErrorCode;
import com.betfair.aping.enums.InstructionReportStatus;
import com.porkerspicks.ggs.domain.types.LayType;

@Entity
public class Lay {
	
	@Id
	private String betId;
	
	private int version;
	private Date timestamp;
	private long selectionId;
	private double price;
	private LayType layType;
	private BetStatus betStatus;
	private InstructionReportStatus status;
	private InstructionReportErrorCode errorCode;
	
	@OneToOne
	private Result result;
	
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
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
	public long getSelectionId() {
		return selectionId;
	}
	public void setSelectionId(long selectionId) {
		this.selectionId = selectionId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public LayType getLayType() {
		return layType;
	}
	public void setLayType(LayType layType) {
		this.layType = layType;
	}
	public BetStatus getBetStatus() {
		return betStatus;
	}
	public void setBetStatus(BetStatus betStatus) {
		this.betStatus = betStatus;
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