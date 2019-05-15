package com.porkerspicks.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import com.porkerspicks.domain.types.BetType;
import com.porkerspicks.domain.types.ResultType;
import com.porkerspicks.domain.types.VenueType;

/**
 * Entity bean for holding details of a blog post.
 * 
 * @author William
 *
 */
@Entity
public class Pick {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Version
	private int version;
	
	private Date timestamp;
	
	private BigDecimal stake;
	
	private BigDecimal price;
	
	private String team;
	
	private String opposition;
	
	private String division;
	
	private VenueType venue;
	
	private BetType betType;
	
	private ResultType forecast;
	
	private ResultType result;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getOpposition() {
		return opposition;
	}

	public void setOpposition(String opposition) {
		this.opposition = opposition;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public VenueType getVenue() {
		return venue;
	}

	public void setVenue(VenueType venue) {
		this.venue = venue;
	}

	public BetType getBetType() {
		return betType;
	}

	public void setBetType(BetType betType) {
		this.betType = betType;
	}

	public ResultType getForecast() {
		return forecast;
	}

	public void setForecast(ResultType forecast) {
		this.forecast = forecast;
	}

	public ResultType getResult() {
		return result;
	}

	public void setResult(ResultType result) {
		this.result = result;
	}
}
