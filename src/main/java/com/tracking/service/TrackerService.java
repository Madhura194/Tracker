package com.tracking.service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracking.dao.ClaimEntity;
import com.tracking.dao.TrackerEntity;
import com.tracking.dao.TrackingSummary;
import com.tracking.dao.UserEntity;
import com.tracking.dao.repo.ClaimRepository;
import com.tracking.dao.repo.TrackerRepository;
import com.tracking.dao.repo.TrackingSummaryRepository;
import com.tracking.dao.repo.UserRepository;
import com.tracking.dto.ClaimEto;
import com.tracking.dto.LoginForm;
import com.tracking.dto.SummaryEto;
import com.tracking.dto.TrackerEto;
import com.tracking.dto.UserEto;

@Service
public class TrackerService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	TrackerRepository trackerRepo;

	@Autowired
	ClaimRepository claimRepo;
	
	@Autowired
	TrackingSummaryRepository summaryRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserEntity signUp(LoginForm signupdetail) {

		UserEntity userEn = new UserEntity();
		userEn.setName(signupdetail.getUsername());
		userEn.setPassword(bCryptPasswordEncoder.encode(signupdetail.getPassword()));

		return userRepo.save(userEn);

	}

	public List<ClaimEto> getClaimsForUser(Principal principal) {

		Optional<UserEntity> userEn = userRepo.findByName(principal.getName());

		if (!userEn.isPresent()) {
			return null;
		}
		List<ClaimEntity> claimEn = claimRepo.findByUser(userEn.get());

		return claimEn.stream().map(this::convertClaim).collect(Collectors.toList());

	}

	public List<UserEto> getAllUsers() {
		List<UserEntity> users = userRepo.findAll();
		return users.stream().map(this::convertUser).collect(Collectors.toList());

	}

	public void saveTrackingEntry(TrackerEto tracker) {

		TrackerEntity trackerEn = new TrackerEntity();

		trackerEn.setActiveTime(tracker.getActiveTime());
		trackerEn.setTrackingDate(new Date());
		UserEntity userEn = userRepo.findById(tracker.getUserId()).get();
		trackerEn.setUser(userEn);
		trackerEn.setType("page_tracking");
		if (tracker.getClaim() != null) {
			ClaimEntity clainEn = new ClaimEntity();
			clainEn = claimRepo.findById(tracker.getClaim().getId()).get();
			trackerEn.setType("claim_tracking");
			trackerEn.setClaim(clainEn);
		}

		trackerRepo.save(trackerEn);
	}
	
	public void calculateSummary(Principal principal) {
		Optional<UserEntity> userEn=userRepo.findByName(principal.getName());
		if(!userEn.isPresent())
			{return;}
		List<TrackerEntity> trackers=trackerRepo.findByUserANDType(userEn.get(), "page_tracking");
		if(trackers.isEmpty()) {
			return;
			}
		Date yesterday=new Date(System.currentTimeMillis()-24*60*60*1000);
		List<TrackerEntity> trackersForYesterday=trackers.stream().filter(t->t.getTrackingDate().compareTo(yesterday)==0).collect(Collectors.toList());
		if(trackersForYesterday.isEmpty()) {
			return;
			}
		Long activeTimeForyesterday=(long) trackersForYesterday.stream().
											collect(Collectors.summarizingLong(TrackerEntity::getActiveTime))
											.getAverage();
		/**************************************************************************************************************************************************/
		trackers.clear();
		trackersForYesterday.clear();
		trackers=trackerRepo.findByUserANDType(userEn.get(), "claim_tracking");
		if(!trackers.isEmpty()) {
			trackersForYesterday=trackers.stream().filter(t->t.getTrackingDate().compareTo(yesterday)==0).collect(Collectors.toList());
			if(!trackersForYesterday.isEmpty()) {
				
				}
			}
		
		
		
		TrackingSummary summEn=new TrackingSummary();
		summEn.setUser(userEn.get());
		summEn.setTrackingDate(yesterday);
		summEn.setAvgActiveTime(activeTimeForyesterday);
		
		
		
		}
	
	public void calculateRewards(Principal principal) {
		
		List<TrackingSummary> summariesForyesterday=summaryRepo.findByTrackingDate(new Date(System.currentTimeMillis()-24*60*60*1000));
		if(summariesForyesterday.isEmpty()) {
			return;
		}
		
		
	}

	
	
	
	
	/****************************************************************************************************************************************************************/
	
	private SummaryEto convertSummary(TrackingSummary trackingSummary) {

		SummaryEto summEto = new SummaryEto();
		UserEto userEto = convertUser(trackingSummary.getUser());
		summEto.setId(trackingSummary.getId());
		summEto.setAvgActiveTime(trackingSummary.getAvgActiveTime());
		summEto.setClaimsProcessed(trackingSummary.getClaimsProcessed());
		summEto.setTrackingDate(trackingSummary.getTrackingDate());
		summEto.setProcessingTimePerClaim(trackingSummary.getProcessingTimePerClaim());
		summEto.setUser(userEto);
		return summEto;
	}

	private TrackerEto convertTracker(TrackerEntity trackerEn) {
		TrackerEto tracker = new TrackerEto();
		tracker.setId(trackerEn.getId());
		tracker.setActiveTime(tracker.getActiveTime());
		tracker.setTrackingDate(trackerEn.getTrackingDate());
		tracker.setType(trackerEn.getType());
		tracker.setUserId(trackerEn.getUser().getId());
		ClaimEto claimEto = new ClaimEto();
		claimEto.setId(trackerEn.getClaim().getId());
		claimEto.setName(trackerEn.getClaim().getName());
		claimEto.setName(trackerEn.getClaim().getStatus());
		tracker.setClaim(claimEto);
		return tracker;

	}

	private ClaimEto convertClaim(ClaimEntity claim) {
		ClaimEto claimEto = new ClaimEto();
		claimEto.setId(claim.getId());
		claimEto.setName(claim.getName());
		claimEto.setStatus(claim.getStatus());
		claimEto.setUserId(claim.getUser().getId());
		return claimEto;
	}

	private UserEto convertUser(UserEntity user) {

		UserEto ueto = new UserEto();
		ueto.setId(user.getId());
		ueto.setName(user.getName());
		ueto.setRewardPoints(user.getRewardPoints());
		return ueto;

	}

}
