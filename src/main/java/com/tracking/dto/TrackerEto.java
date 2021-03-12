package com.tracking.dto;

import java.util.Date;

public class TrackerEto {
	
	private Long id;
	
	private Date trackingDate;
	
	
	private Long activeTime;
	
	private Long userId;
	
	private String type;
	

	private ClaimEto claim;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getTrackingDate() {
		return trackingDate;
	}


	public void setTrackingDate(Date trackingDate) {
		this.trackingDate = trackingDate;
	}


	public Long getActiveTime() {
		return activeTime;
	}


	public void setActiveTime(Long activeTime) {
		this.activeTime = activeTime;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public ClaimEto getClaim() {
		return claim;
	}


	public void setClaim(ClaimEto claim) {
		this.claim = claim;
	}
	
	
	
	
	
	
	
	
	
}
