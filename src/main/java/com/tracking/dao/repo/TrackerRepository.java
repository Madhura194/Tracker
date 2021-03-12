package com.tracking.dao.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tracking.dao.ClaimEntity;
import com.tracking.dao.TrackerEntity;
import com.tracking.dao.UserEntity;

public interface TrackerRepository extends JpaRepository<TrackerEntity, Long> {
	
	@Query("select t from TrackerEntity t where t.user = :user AND t.type = :type")
	public List<TrackerEntity> findByUserANDType(@Param("user")UserEntity user,@Param("type")String type);
	
	public List<TrackerEntity> findByClaims(ClaimEntity claims);
	
	
	
	
 
}
