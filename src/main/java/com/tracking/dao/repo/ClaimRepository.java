package com.tracking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tracking.dao.ClaimEntity;
import com.tracking.dao.UserEntity;

public interface ClaimRepository extends JpaRepository<ClaimEntity, Long>{

	public 	 List<ClaimEntity> findByUser(UserEntity user);

	public 	 List<ClaimEntity> findByStatus(String status);

	 
}
