package com.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.tracking.dto.TrackerEto;
import com.tracking.service.TrackerService;

@Controller
@CrossOrigin(origins="*")
public class WebSocketController {
	
	@Autowired
	TrackerService trackerSer;
	

	@MessageMapping("/track/welcome")
	@SendTo("/topic/track/welcome")
	public String SaveActiveTimeStamp() {
		return "Activity Recording started!";
	}
	
	
	@MessageMapping("/track/{userId}")
	public String saveTrackingtime(@DestinationVariable("userId") Long userId,@Payload TrackerEto tracker) {
		trackerSer.saveTrackingEntry(tracker);
		return "tracker upadted !";
	}
	
	@MessageMapping("/track/claim/{id}")
	public String saveClaimTracking(@DestinationVariable("id") Long id,@Payload TrackerEto tracker) {
		
		return "claim tracking entry saved !";
	}
	
	
}
