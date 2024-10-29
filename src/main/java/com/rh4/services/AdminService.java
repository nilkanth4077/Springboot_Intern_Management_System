package com.rh4.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rh4.entities.*;
import com.rh4.repositories.*;

@Service
public class AdminService {

	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private InternService internService;
	@Autowired
	private CancelledRepo cancelledRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private MyUserService myUserService;

	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public void registerAdmin(Admin admin) {
		// save to admin table
		String encryptedPassword = passwordEncoder().encode(admin.getPassword());
		admin.setPassword(encryptedPassword);
		adminRepo.save(admin);
		// save to user table
		MyUser user = new MyUser();
		user.setUsername(admin.getEmailId());
		// encrypt password
		user.setPassword(encryptedPassword);
		user.setRole("ADMIN");
		user.setEnabled(true);
		// from long to string
		String userId = Long.toString(admin.getAdminId());
		user.setUserId(userId);
		userRepo.save(user);
		System.out.println(user.getId());
	}

	public List<Admin> getAdmin() {
		return adminRepo.findAll();
	}

	public Optional<Admin> getAdmin(long id) {
		return adminRepo.findById(id);
	}

	public void deleteAdmin(long id) {
		adminRepo.deleteById(id);
	}

	public void updateAdmin(Admin updatedAdmin, Optional<Admin> existingAdmin) {

		if (updatedAdmin.getEmailId().equals(existingAdmin.get().getEmailId())) {
			adminRepo.save(updatedAdmin);
			String adminId = Long.toString(updatedAdmin.getAdminId());
			String password = updatedAdmin.getPassword();
			String emailId = updatedAdmin.getEmailId();
			userRepo.updateAdminUser(adminId, password, emailId, "ADMIN");
		} else {
			if (userRepo.findByUsername(updatedAdmin.getEmailId()) == null) {
				// If it doesn't exist, proceed with the update
				adminRepo.save(updatedAdmin);
				String adminId = Long.toString(updatedAdmin.getAdminId());
				String password = updatedAdmin.getPassword();
				String emailId = updatedAdmin.getEmailId();
				userRepo.updateAdminUser(adminId, password, emailId, "ADMIN");
			} else {
				// Handle the case where the email already exists
				// You can throw an exception, log a message, or take other appropriate action
				// For now, let's print a message to the console
				System.out.println("Email already exists: " + updatedAdmin.getEmailId());
			}
		}

	}

	public Admin getAdminByUsername(String username) {
		return adminRepo.findByEmailId(username);
	}

	public void changePassword(Admin admin, String newPassword) {
		String encryptedPassword = passwordEncoder().encode(newPassword);
		admin.setPassword(encryptedPassword);
		adminRepo.save(admin);
		// save to user table
		MyUser user = myUserService.getUserByUsername(admin.getEmailId());
		user.setPassword(encryptedPassword);
		userRepo.save(user);
	}

	public void cancelIntern(String cancelAns, String internId) {
		Optional<Intern> optionalIntern = internService.getIntern(internId);

		if (optionalIntern.isPresent()) {
			Intern intern = optionalIntern.get();

			if ("approve".equals(cancelAns)) {
				// Handle approval
				intern.setCancellationStatus("cancelled");
				intern.setIsActive(false);
				// Save the intern to the cancelled table
				Cancelled cancelledEntry = new Cancelled();
				cancelledEntry.setTableName("intern");
				cancelledEntry.setCancelId(intern.getInternId());
				cancelledRepo.save(cancelledEntry);
			} else if ("reject".equals(cancelAns)) {
				// Handle rejection
				intern.setCancellationStatus(null);
			}

			internService.updateCancellationStatus(intern);
		}
	}

}