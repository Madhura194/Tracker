package com.tracking.dao.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tracking.dao.TrackingSummary;
import com.tracking.dao.UserEntity;

public interface TrackingSummaryRepository  extends JpaRepository<TrackingSummary,Long>{

 List<TrackingSummary> findByTrackingDate(Date trackingDate);
 
 @Query("select t from TrackingSummary t where t.user = :user AND t.trackingDate = :trackingDate")
 List<TrackingSummary> findByUserANDTrackingDate(UserEntity user,Date trackingDate);
 
 public List<TrackingSummary> findByUser(UserEntity user);
 
 
 
}
