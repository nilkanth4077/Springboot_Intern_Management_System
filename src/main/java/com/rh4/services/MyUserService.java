package com.rh4.services;

import com.rh4.exception.UserException;
import com.rh4.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rh4.repositories.*;
import com.rh4.entities.MyUser;

import java.util.Optional;

@Service
public class MyUserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JwtUtil jwtUtil;
	
	public MyUser getUserByUsername(String email) {
		return userRepo.findByUsername(email);
	}

	public Optional<MyUser> getProfileByToken(String token) throws UserException {
		String email = jwtUtil.extractEmail(token);

		Optional<MyUser> user = userRepo.findByEmail(email);
		if (user.isEmpty()) {
			throw new UserException("User not found with email: " + email);
		}
		return user;
	}

}