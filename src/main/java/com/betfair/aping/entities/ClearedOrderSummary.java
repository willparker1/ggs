package com.betfair.aping.entities;

import java.util.Date;

import com.betfair.aping.enums.OrderType;
import com.betfair.aping.enums.PersistenceType;
import com.betfair.aping.enums.Side;

public class ClearedOrderSummary {
	
	private String eventTypeId;
	private String eventId;
	private String marketId;
	private long selectionId;
	private double handicap;
	private String betId;
	private Date placedDate;
	private PersistenceType persistenceType;
	private OrderType orderType;
	private Side side;
	private ItemDescription itemDescription;
	private String betOutcome;
	private double priceRequested;
	private Date settledDate;
	private Date lastMatchedDate;
	private int betCount;
	private double commission;
	private double priceMatched;
	private boolean priceReduced;
	private double sizeSettled;
	private double profit;
	private double sizeCancelled;
	private String customerOrderRef;
	private String customerStrategyRef;
	
	
	public String getEventTypeId() {
		return eventTypeId;
	}
	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public long getSelectionId() {
		return selectionId;
	}
	public void setSelectionId(long selectionId) {
		this.selectionId = selectionId;
	}
	public double getHandicap() {
		return handicap;
	}
	public void setHandicap(double handicap) {
		this.handicap = handicap;
	}
	public String getBetId() {
		return betId;
	}
	public void setBetId(String betId) {
		this.betId = betId;
	}
	public Date getPlacedDate() {
		return placedDate;
	}
	public void setPlacedDate(Date placedDate) {
		this.placedDate = placedDate;
	}
	public PersistenceType getPersistenceType() {
		return persistenceType;
	}
	public void setPersistenceType(PersistenceType persistenceType) {
		this.persistenceType = persistenceType;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	public Side getSide() {
		return side;
	}
	public void setSide(Side side) {
		this.side = side;
	}
	public ItemDescription getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(ItemDescription itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getBetOutcome() {
		return betOutcome;
	}
	public void setBetOutcome(String betOutcome) {
		this.betOutcome = betOutcome;
	}
	public double getPriceRequested() {
		return priceRequested;
	}
	public void setPriceRequested(double priceRequested) {
		this.priceRequested = priceRequested;
	}
	public Date getSettledDate() {
		return settledDate;
	}
	public void setSettledDate(Date settledDate) {
		this.settledDate = settledDate;
	}
	public Date getLastMatchedDate() {
		return lastMatchedDate;
	}
	public void setLastMatchedDate(Date lastMatchedDate) {
		this.lastMatchedDate = lastMatchedDate;
	}
	public int getBetCount() {
		return betCount;
	}
	public void setBetCount(int betCount) {
		this.betCount = betCount;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}
	public double getPriceMatched() {
		return priceMatched;
	}
	public void setPriceMatched(double priceMatched) {
		this.priceMatched = priceMatched;
	}
	public boolean isPriceReduced() {
		return priceReduced;
	}
	public void setPriceReduced(boolean priceReduced) {
		this.priceReduced = priceReduced;
	}
	public double getSizeSettled() {
		return sizeSettled;
	}
	public void setSizeSettled(double sizeSettled) {
		this.sizeSettled = sizeSettled;
	}
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public double getSizeCancelled() {
		return sizeCancelled;
	}
	public void setSizeCancelled(double sizeCancelled) {
		this.sizeCancelled = sizeCancelled;
	}
	public String getCustomerOrderRef() {
		return customerOrderRef;
	}
	public void setCustomerOrderRef(String customerOrderRef) {
		this.customerOrderRef = customerOrderRef;
	}
	public String getCustomerStrategyRef() {
		return customerStrategyRef;
	}
	public void setCustomerStrategyRef(String customerStrategyRef) {
		this.customerStrategyRef = customerStrategyRef;
	}
}
