package com.tracking.dao;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tracking_summary")
public class TrackingSummary {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date trackingDate;
	private Integer claimsProcessed;
	private Long avgActiveTime;
	private Long processingTimePerClaim;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
	private UserEntity user;

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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
	
	
}
