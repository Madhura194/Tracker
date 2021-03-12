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
@Table(name="tracker")
public class TrackerEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Date trackingDate;
	
	private Long activeTime;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
	private UserEntity user;
	
	private String type;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "claimTracker", referencedColumnName = "id")
	private ClaimEntity claims;

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	public ClaimEntity getClaim() {
		return claims;
	}

	public void setClaim(ClaimEntity claim) {
		this.claims = claim;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

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

	

	
	
	
	
	
}
