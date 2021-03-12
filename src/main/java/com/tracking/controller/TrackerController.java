package com.tracking.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracking.dto.ClaimEto;
import com.tracking.dto.UserEto;
import com.tracking.service.TrackerService;

@RestController
public class TrackerController {
	
	@Autowired
	TrackerService trackerSer;
	
	
	
	@GetMapping("/hi")
	public String hello() {
		return "welcome to tracker!";
	}

	
	@GetMapping("/claims")
	public List<ClaimEto> getClaims(HttpServletRequest request)
	{
		return trackerSer.getClaimsForUser(request.getUserPrincipal());
		
	}
	
	@GetMapping("/users")
	public List<UserEto> getAllUsers() {
		return trackerSer.getAllUsers();
	}
	
	
	
	
	
}
