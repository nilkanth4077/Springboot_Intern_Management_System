package com.rh4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rh4.repositories.*;
import com.rh4.entities.MyUser;

@Service
public class MyUserService {

	@Autowired
	private UserRepo userRepo;
	
	public MyUser getUserByUsername(String email) {
		return userRepo.findByUsername(email);
	}

}