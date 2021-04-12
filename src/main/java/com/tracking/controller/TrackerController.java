package com.tracking.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tracking.dto.ClaimEto;
import com.tracking.dto.SummaryEto;
import com.tracking.dto.TrackerEto;
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
	
	@PostMapping("/dummy/tracker")
	public String dummysavetrackerEntry(HttpServletRequest request,@RequestBody TrackerEto tracker) {
		trackerSer.saveTrackingEntry(tracker);
		return "tracking entry saved !!";
	}
	
	@GetMapping("/dummy/consolidate")
	public SummaryEto consolidateSummary(HttpServletRequest request){
		//trackerSer.calculateRewardsForYesterday(request.getUserPrincipal());
		return new SummaryEto(); //trackerSer.calculateSummary(request.getUserPrincipal());
	}
	
	
	
	
}
