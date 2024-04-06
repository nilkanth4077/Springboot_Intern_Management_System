package com.rh4.repositories;
import com.rh4.entities.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<GroupEntity,Long>{

	GroupEntity findTopByOrderByGroupIdDesc();
	
	@Query("FROM GroupEntity g WHERE g.guide IS NULL")
	public List<GroupEntity> getGroupEntityNoGuide();

	GroupEntity getByGroupId(String id);

	@Query("FROM GroupEntity g WHERE g.guide IS NOt NULL")
	List<GroupEntity> getAllocatedGroups();

	@Query("FROM GroupEntity g WHERE g.guide IS NULL")
	List<GroupEntity> getNotAllocatedGroups();

	List<GroupEntity> findByProjectDefinitionStatus(String string);
	
	@Query("from GroupEntity g where g.guide = :guide")
	public List<GroupEntity> getInternGroups(@Param("guide") Guide guide);

	long countByProjectDefinitionStatus(String projectDefinitionStatus);
	
	List<GroupEntity> findByGuideAndProjectDefinitionStatus(Guide guide, String projectDefinitionStatus);

	List<GroupEntity> findByGuideAndFinalReportStatus(Guide guide, String finalReportStatus);

	List<GroupEntity> findByFinalReportStatus(String finalReportStatus);

	
}
