package com.rh4.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh4.entities.GroupEntity;
import com.rh4.entities.Guide;
@Repository
public interface GuideRepo extends JpaRepository<Guide, Long>{

	public Guide findByEmailId(String username);
	public Optional<Guide> findByName(String name);
	

}
