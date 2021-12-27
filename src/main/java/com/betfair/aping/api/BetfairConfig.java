package com.betfair.aping.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="betfair")
public class BetfairConfig {

	private String sessionId;
	private String applicationKey;
	private String apiUrl = null;
	private String contentType = null;
	private String encoding = null;
	private int timeOut = 10000;
	private double betSize;
	private double laySize;
	private double mediumFieldThrehhold;
	private double largeFieldThrehhold; 
	private double fanciedSmallFieldPriceThreshhold;
	private double fanciedMediumFieldPriceThreshhold;
	private double fanciedLargeFieldPriceThreshhold;
	private double shortPricedFanciedReductionFactor;
	private double mediumPricedFanciedReductionFactor;
	private double largePricedFanciedReductionFactor;
	private double favouriteThreshhold;
	private double favouriteReductionFactor;  
	private double hotFavouriteReductionFactor;  
	private double oddsOnLaySize;
	private double[] oddsOnTiers;
	private double sizeOfMarket;

	private boolean debug = false;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(String applicationId) {
		this.applicationKey = applicationId;
	}

	public double getBetSize() {
		return betSize;
	}

	public void setBetSize(double betSize) {
		this.betSize = betSize;
	}

	public boolean isDebug() {
		return debug;
	}

	public double getLaySize() {
		return laySize;
	}

	public void setLaySize(double laySize) {
		this.laySize = laySize;
	}

	public double getMediumFieldThrehhold() {
		return mediumFieldThrehhold;
	}

	public void setMediumFieldThrehhold(double mediumFieldThrehhold) {
		this.mediumFieldThrehhold = mediumFieldThrehhold;
	}

	public double getLargeFieldThrehhold() {
		return largeFieldThrehhold;
	}

	public void setLargeFieldThrehhold(double largeFieldThrehhold) {
		this.largeFieldThrehhold = largeFieldThrehhold;
	}

	public double getFanciedSmallFieldPriceThreshhold() {
		return fanciedSmallFieldPriceThreshhold;
	}

	public void setFanciedSmallFieldPriceThreshhold(double fanciedSmallFieldPriceThreshhold) {
		this.fanciedSmallFieldPriceThreshhold = fanciedSmallFieldPriceThreshhold;
	}

	public double getFanciedMediumFieldPriceThreshhold() {
		return fanciedMediumFieldPriceThreshhold;
	}

	public void setFanciedMediumFieldPriceThreshhold(double fanciedMediumFieldPriceThreshhold) {
		this.fanciedMediumFieldPriceThreshhold = fanciedMediumFieldPriceThreshhold;
	}

	public double getFanciedLargeFieldPriceThreshhold() {
		return fanciedLargeFieldPriceThreshhold;
	}

	public void setFanciedLargeFieldPriceThreshhold(double fanciedLargeFieldPriceThreshhold) {
		this.fanciedLargeFieldPriceThreshhold = fanciedLargeFieldPriceThreshhold;
	}

	public double getHotFavouriteReductionFactor() {
		return hotFavouriteReductionFactor;
	}

	public void setHotFavouriteReductionFactor(double hotFavouriteReductionFactor) {
		this.hotFavouriteReductionFactor = hotFavouriteReductionFactor;
	}

	public double getShortPricedFanciedReductionFactor() {
		return shortPricedFanciedReductionFactor;
	}

	public void setShortPricedFanciedReductionFactor(double shortPricedFanciedReductionFactor) {
		this.shortPricedFanciedReductionFactor = shortPricedFanciedReductionFactor;
	}

	public double getMediumPricedFanciedReductionFactor() {
		return mediumPricedFanciedReductionFactor;
	}

	public void setMediumPricedFanciedReductionFactor(double mediumPricedFanciedReductionFactor) {
		this.mediumPricedFanciedReductionFactor = mediumPricedFanciedReductionFactor;
	}

	public double getLargePricedFanciedReductionFactor() {
		return largePricedFanciedReductionFactor;
	}

	public void setLargePricedFanciedReductionFactor(double largePricedFanciedReductionFactor) {
		this.largePricedFanciedReductionFactor = largePricedFanciedReductionFactor;
	}

	public double getFavouriteThreshhold() {
		return favouriteThreshhold;
	}

	public void setFavouriteThreshhold(double favouriteThreshhold) {
		this.favouriteThreshhold = favouriteThreshhold;
	}

	public double getFavouriteReductionFactor() {
		return favouriteReductionFactor;
	}

	public void setFavouriteReductionFactor(double favouriteReductionFactor) {
		this.favouriteReductionFactor = favouriteReductionFactor;
	}

	public double getSizeOfMarket() {
		return sizeOfMarket;
	}

	public void setSizeOfMarket(double sizeOfMarket) {
		this.sizeOfMarket = sizeOfMarket;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public double[] getOddsOnTiers() {
		return oddsOnTiers;
	}

	public double getOddsOnLaySize() {
		return oddsOnLaySize;
	}

	public void setOddsOnLaySize(double oddsOnLaySize) {
		this.oddsOnLaySize = oddsOnLaySize;
	}

	public double getOddsOnTier( int index ) {
		return oddsOnTiers[index];
	}

	public void setOddsOnTiers(double[] oddsOnTiers) {
		this.oddsOnTiers = oddsOnTiers;
	}

	public String getApiUrl() {
		return apiUrl;
	}
	
    public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}    
}
