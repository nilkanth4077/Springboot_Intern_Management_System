package com.rh4.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh4.entities.Cancelled;
import com.rh4.repositories.CancelledRepo;

@Service
public class CancelledService {
	
	@Autowired
	private CancelledRepo cancelledRepo;

	public List<Cancelled> getCancelledIntern() {
		return cancelledRepo.getCancelledIntern();
	}
	
}