package com.porkerspicks.horses.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.porkerspicks.horses.domain.types.LayType;

@Entity
public class CandidateBet {	
	
	@Id
	private Long betId;
	
	private int version;
	
	private LayType candidateType;
	
	private double targetPrice;
	
	private boolean betPlaced;
	
	public Long getBetId() {
		return betId;
	}
	public void setBetId(Long betId) {
		this.betId = betId;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public LayType getCandidateType() {
		return candidateType;
	}
	public void setCandidateType(LayType candidateType) {
		this.candidateType = candidateType;
	}
	public boolean isBetPlaced() {
		return betPlaced;
	}
	public void setBetPlaced(boolean betPlaced) {
		this.betPlaced = betPlaced;
	}
	public double getTargetPrice() {
		return targetPrice;
	}
	public void setTargetPrice(double targetPrice) {
		this.targetPrice = targetPrice;
	}
}