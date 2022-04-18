package com.porkerspicks.ggs.service.events;

import org.springframework.context.ApplicationEvent;

import com.porkerspicks.ggs.domain.Bet;

public class BetEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	
	private String eventType;
	private Bet bet;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Bet getBet() {
		return bet;
	}

	public void setBet(Bet bet) {
		this.bet = bet;
	}

	public BetEvent(String eventType, Bet bet) {
		super(bet);
		this.eventType = eventType;
		this.bet = bet;
	}

	@Override
	public String toString() {
		return "Bet Event [eventType=" + eventType + ", bet=" + bet + "]";
	}
}
