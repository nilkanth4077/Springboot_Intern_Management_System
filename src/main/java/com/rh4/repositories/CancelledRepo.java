package com.rh4.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.rh4.entities.Cancelled;


@Repository
public interface CancelledRepo extends CrudRepository<Cancelled,Long>{
	
	@Query("FROM Cancelled c WHERE c.tableName='intern'")
	List<Cancelled> getCancelledIntern();
	
}

