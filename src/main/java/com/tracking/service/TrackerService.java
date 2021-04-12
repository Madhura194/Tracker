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
import com.tracking.dao.RewardPoints;
import com.tracking.dao.TrackerEntity;
import com.tracking.dao.TrackingSummary;
import com.tracking.dao.UserEntity;
import com.tracking.dao.repo.ClaimRepository;
import com.tracking.dao.repo.RewardPointsRepo;
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
	RewardPointsRepo rwrdRepo;

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
		trackerEn.setTrackingDate(new Date(System.currentTimeMillis()-24*60*60*1000));//(new Date())
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
	
	public SummaryEto calculateSummary(Principal principal) {
		Optional<UserEntity> userEn=userRepo.findByName(principal.getName());
		if(!userEn.isPresent())
			{return null;}
		List<TrackerEntity> trackers=trackerRepo.findByUserANDType(userEn.get(), "page_tracking");
		for (TrackerEntity trackerEntity : trackers) {
			System.out.println(trackerEntity.getUser().getName()+" type: "+trackerEntity.getType());
		}
		if(trackers.isEmpty()) {
			return null;
			}
		Date yesterday=new Date(System.currentTimeMillis()-24*60*60*1000);
		System.out.println("YESTERDAY: "+yesterday);
		List<TrackerEntity> trackersForYesterday=trackers.stream().filter(t->t.getTrackingDate().before(new Date())).collect(Collectors.toList());
		if(trackersForYesterday.isEmpty()) {
			return null;
			}
		Long activeTimeForyesterday=(long) trackersForYesterday.stream().
											collect(Collectors.summarizingLong(TrackerEntity::getActiveTime))
											.getSum();
		
		System.out.println("activeTimeForyesterday: "+activeTimeForyesterday);
		/**************************************************************************************************************************************************/
		trackers.clear();
		trackersForYesterday.clear();
		trackers=trackerRepo.findByUserANDType(userEn.get(), "claim_tracking");
		Long avgClaimProcessingTime=0L;
		int claimsProcessed=0;
		if(!trackers.isEmpty()) {
			trackersForYesterday=trackers.stream().filter(t->t.getTrackingDate().before(new Date())).collect(Collectors.toList());
			if(!trackersForYesterday.isEmpty()) {
				for (TrackerEntity trackerEntity : trackersForYesterday) {
					System.out.println(trackerEntity.getActiveTime()+"   "+trackerEntity.getType());
					avgClaimProcessingTime=avgClaimProcessingTime+trackerEntity.getActiveTime();
				}
				claimsProcessed=trackersForYesterday.size();
				}
			}
		
		
		
		TrackingSummary summEn=new TrackingSummary();
		summEn.setUser(userEn.get());
		summEn.setTrackingDate(yesterday);
		summEn.setAvgActiveTime(activeTimeForyesterday);
		summEn.setClaimsProcessed(claimsProcessed);
		summEn.setProcessingTimePerClaim((long) (avgClaimProcessingTime/claimsProcessed));
		
		return convertSummary(summaryRepo.save(summEn));
		
		}
	
	public void calculateRewardsForYesterday(Principal principal) {
		
		Long totalActiveTime=0L;
		Long avgClaimProcessingTime=0L;
		int totalClaims=0;
		int rwrd=0;
		Date yesterday=null;
		Optional<UserEntity> userEn=userRepo.findByName(principal.getName());
		if(!userEn.isPresent())
			{return;}
		List<TrackingSummary> summaries=summaryRepo.findByUser(userEn.get());
		List<TrackingSummary> summariesForyesterday=summaries.stream().filter(t->t.getTrackingDate().before(new Date())).collect(Collectors.toList());
		if(summariesForyesterday.isEmpty()) {
			return;
		}
		for (TrackingSummary trackingSummary : summariesForyesterday) {
			totalActiveTime=totalActiveTime+trackingSummary.getAvgActiveTime();
			avgClaimProcessingTime=avgClaimProcessingTime+trackingSummary.getProcessingTimePerClaim();
			totalClaims=totalClaims+trackingSummary.getClaimsProcessed();
			yesterday=trackingSummary.getTrackingDate();
		}
		
		if(totalActiveTime> 300L) {
			rwrd=rwrd+2;
			}
	if(totalClaims>5 ) {
		rwrd=rwrd+2;
	}
	if(avgClaimProcessingTime >100L) {
		rwrd=rwrd+2;
	}
	
		RewardPoints rewardEn=new RewardPoints();
		rewardEn.setDate(yesterday);
		rewardEn.setRewardsForDay(rwrd);
		rewardEn.setUser(userEn.get());
		
		rwrdRepo.save(rewardEn);
		
		
		
		
		
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
