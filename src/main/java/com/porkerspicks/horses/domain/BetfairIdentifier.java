package com.porkerspicks.horses.domain;

/**
 * 
 * @author wullp
 *
 * Holds the identifiers for a horse bet selection.
 *
 */
public class BetfairIdentifier {
	
	private String marketId;
	private Long selectionId;
	
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public Long getSelectionId() {
		return selectionId;
	}
	public void setSelectionId(Long selectionId) {
		this.selectionId = selectionId;
	}
}
