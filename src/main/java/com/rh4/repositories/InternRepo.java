package com.rh4.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rh4.entities.*;

@Repository
public interface InternRepo extends JpaRepository<Intern, String> {

	Intern findTopByOrderByInternIdDesc();

	Intern findByEmail(String username);

	List<Intern> findByGroupId(long groupId);

	List<Intern> findByCancellationStatus(String cancellationStatus);

	long countByCancellationStatus(String cancellationStatus);

	@Query("SELECT i FROM Intern i WHERE i.isActive = false")
	List<Intern> getCancelledIntern();

	@Query("SELECT i FROM Intern i WHERE i.isActive = true")
	List<Intern> getCurrentInterns();

	@Query("SELECT i FROM Intern i " + "WHERE (:college IS NULL OR i.collegeName = :college) "
			+ "AND (:branch IS NULL OR i.branch = :branch) " + "AND (:guide IS NULL OR i.guide = :guide) "
			+ "AND (:domain IS NULL OR i.programmingLangName = :domain) "
			+ "AND (:startDate IS NULL OR i.joiningDate >= :startDate) "
			+ "AND (:endDate IS NULL OR i.completionDate <= :endDate)" + "AND (i.isActive =:cancelled)")
	List<Intern> getFilteredInterns(@Param("college") String college, @Param("branch") String branch,
			@Param("guide") Optional<Guide> guide, @Param("domain") String domain, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("cancelled") boolean cancelled);

	@Query("SELECT i FROM Intern i " + "INNER JOIN i.group g "
			+ "WHERE (:college IS NULL OR i.collegeName = :college) " + "AND (:branch IS NULL OR i.branch = :branch) "
			+ "AND (:guide IS NULL OR g.guide = :guide) " + "AND (:domain IS NULL OR i.programmingLangName = :domain) "
			+ "AND (:startDate IS NULL OR i.joiningDate >= :startDate) "
			+ "AND (:endDate IS NULL OR i.completionDate <= :endDate)" + "AND g.finalReportStatus = 'pending'")
	List<Intern> getPendingInternsFilter(String college, String branch, Optional<Guide> guide, String domain,
			Date startDate, Date endDate);

}