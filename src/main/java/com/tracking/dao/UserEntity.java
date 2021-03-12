package com.tracking.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class UserEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String password;
	
	@OneToMany(mappedBy = "user")
	private List<TrackerEntity> trackerEntries;
	
	@OneToMany(mappedBy = "user")
	private List<ClaimEntity> claims;
	
	@OneToMany(mappedBy = "user")
	private List<TrackingSummary> summaries;
	
	private Integer rewardPoints;
	
	public Integer getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(Integer rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public List<TrackerEntity> getTrackerEntries() {
		return trackerEntries;
	}
	public void setTrackerEntries(List<TrackerEntity> trackerEntries) {
		this.trackerEntries = trackerEntries;
	}
	public List<ClaimEntity> getClaims() {
		return claims;
	}
	public void setClaims(List<ClaimEntity> claims) {
		this.claims = claims;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	

}
