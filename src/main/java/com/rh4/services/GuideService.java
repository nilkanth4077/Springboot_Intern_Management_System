package com.rh4.services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rh4.entities.GroupEntity;
import com.rh4.entities.Guide;
import com.rh4.entities.MyUser;
import com.rh4.repositories.GroupRepo;
import com.rh4.repositories.GuideRepo;
import com.rh4.repositories.UserRepo;

@Service
public class GuideService {

	@Autowired
	private GuideRepo guideRepo;
	@Autowired
	private GroupRepo groupRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private MyUserService myUserService;

	PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	public void registerGuide(Guide guide)
	{

		String encryptedPassword = passwordEncoder().encode(guide.getPassword());
		guide.setPassword(encryptedPassword);
		guideRepo.save(guide);		
		//save to user table
		MyUser user = new MyUser();
		user.setUsername(guide.getEmailId());
		//encrypt password
		user.setPassword(encryptedPassword);
		user.setRole("GUIDE");
		user.setEnabled(true);
		//from long to string
		String userId = Long.toString(guide.getGuideId());
		user.setUserId(userId);
		userRepo.save(user);
		System.out.println(user.getId());
	}

	public List<Guide> getGuide() {
		// TODO Auto-generated method stub
		return guideRepo.findAll();
	}
	
	public Optional<Guide> getGuide(long id) {
		return guideRepo.findById(id);
	}
	public Optional<Guide> getGuideById(long id) {
		return guideRepo.findById(id);
	}
	
	public Optional<Guide> getGuideByName(String name) {
		return guideRepo.findByName(name);
	}
	
	public void updateGuide(Guide updatedGuide) {
		// TODO Auto-generated method stub
		//guideRepo.save(updatedGuide);
		 if (userRepo.findByUsername(updatedGuide.getEmailId()) == null) {
		        // If it doesn't exist, proceed with the update
		        guideRepo.save(updatedGuide);
		        String guideId = Long.toString(updatedGuide.getGuideId());
		        String password = updatedGuide.getPassword();
		        String emailId = updatedGuide.getEmailId();
		        userRepo.updateGuideUser(guideId, password, emailId,"GUIDE");
		    } else {
		        
		        System.out.println("Email already exists: " + updatedGuide.getEmailId());
		    }
	}

	public void deleteGuide(long id) {
		// TODO Auto-generated method stub
		guideRepo.deleteById(id);
	}
	public long countGuides() {
		return guideRepo.count();
	}
	public void updateGuide(Guide updatedGuide, Optional<Guide> existingGuide) {
		if(updatedGuide.getEmailId().equals(existingGuide.get().getEmailId()))
		{
			guideRepo.save(updatedGuide);
	        String guideId = Long.toString(updatedGuide.getGuideId());
	        String password = updatedGuide.getPassword();
	        String emailId = updatedGuide.getEmailId();
	        userRepo.updateGuideUser(guideId, password, emailId,"GUIDE");
		}
		else
		{
			if (userRepo.findByUsername(updatedGuide.getEmailId()) == null) {
		        // If it doesn't exist, proceed with the update
				guideRepo.save(updatedGuide);
		        String guideId = Long.toString(updatedGuide.getGuideId());
		        String password = updatedGuide.getPassword();
		        String emailId = updatedGuide.getEmailId();
		        userRepo.updateGuideUser(guideId, password, emailId,"GUIDE");
		    }
			else {
		        // Handle the case where the email already exists
		        // You can throw an exception, log a message, or take other appropriate action
		        // For now, let's print a message to the console
		        System.out.println("Email already exists: " + updatedGuide.getEmailId());
		    }
		}
	
	}
	public Guide getGuideByUsername(String username) {
		// TODO Auto-generated method stub
		return guideRepo.findByEmailId(username);
	}
	public List<GroupEntity> getInternGroups(Guide guide) {
		
		return groupRepo.getInternGroups(guide);
	}
	public void changePassword(Guide guide, String newPassword) {
		String encryptedPassword = passwordEncoder().encode(newPassword);
		guide.setPassword(encryptedPassword);
		guideRepo.save(guide);
		// save to user table
		MyUser user = myUserService.getUserByUsername(guide.getEmailId());
		user.setPassword(encryptedPassword);
		userRepo.save(user);
	}
	
}