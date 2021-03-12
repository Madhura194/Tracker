package com.tracking.dao.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tracking.dao.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	Optional<UserEntity> findByName(String name);
	
	
}
