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
public class SuperAdminService {
	
	@Autowired
	private SuperAdminRepo superAdminRepo;
	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private UserRepo userRepo;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	public void registerSuperAdmin(SuperAdmin superAdmin)
	{
		//save to admin table
		String encryptedPassword = passwordEncoder().encode(superAdmin.getPassword());
		superAdmin.setPassword(encryptedPassword);
		superAdminRepo.save(superAdmin);		
		//save to user table
		MyUser user = new MyUser();
		user.setUsername(superAdmin.getEmailId());
		//encrypt password
		user.setPassword(encryptedPassword);
		user.setRole("ADMIN");
		user.setEnabled(true);
		//from long to string
		String userId = Long.toString(superAdmin.getSuperAdminId());
		user.setUserId(userId);
		userRepo.save(user);
	}

	public SuperAdmin getSuperAdminByUsername(String username)
	{
		return superAdminRepo.findByEmailId(username);
	}
	
	public long countAdmin() {
        return adminRepo.count();
    }
}