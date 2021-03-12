package com.tracking.dto;

import java.util.Date;

public class SummaryEto {
	
	private Long id;
	private Date trackingDate;
	private Integer claimsProcessed;
	private Long avgActiveTime;
	private Long processingTimePerClaim;
	private UserEto user;
	
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
	public Integer getClaimsProcessed() {
		return claimsProcessed;
	}
	public void setClaimsProcessed(Integer claimsProcessed) {
		this.claimsProcessed = claimsProcessed;
	}
	public Long getAvgActiveTime() {
		return avgActiveTime;
	}
	public void setAvgActiveTime(Long avgActiveTime) {
		this.avgActiveTime = avgActiveTime;
	}
	public Long getProcessingTimePerClaim() {
		return processingTimePerClaim;
	}
	public void setProcessingTimePerClaim(Long processingTimePerClaim) {
		this.processingTimePerClaim = processingTimePerClaim;
	}
	public UserEto getUser() {
		return user;
	}
	public void setUser(UserEto user) {
		this.user = user;
	}
	
	
	

}
