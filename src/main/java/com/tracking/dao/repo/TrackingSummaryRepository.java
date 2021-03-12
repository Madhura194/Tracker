package com.tracking.dao.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tracking.dao.TrackingSummary;

public interface TrackingSummaryRepository  extends JpaRepository<TrackingSummary,Long>{

 List<TrackingSummary> findByTrackingDate(Date trackingDate);
 
}
