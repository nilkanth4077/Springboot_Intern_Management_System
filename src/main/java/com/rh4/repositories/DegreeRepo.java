package com.rh4.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rh4.entities.*;

public interface DegreeRepo extends JpaRepository<Degree, Long> {

	Degree findByName(String name);
 
}