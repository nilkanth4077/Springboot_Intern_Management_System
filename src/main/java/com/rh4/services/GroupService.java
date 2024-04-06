package com.rh4.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rh4.entities.GroupEntity;
import com.rh4.entities.Guide;
import com.rh4.entities.Intern;
import com.rh4.repositories.GroupRepo;
@Service
public class GroupService {

	@Autowired
	private GroupRepo groupRepo;
	@Autowired
	private GuideService guideService;
	
	public String getMostRecentGroupId()
	{
		GroupEntity mostRecentGroup = groupRepo.findTopByOrderByGroupIdDesc();
        return mostRecentGroup != null ? mostRecentGroup.getGroupId() : null;
	}
	
	public void registerGroup(GroupEntity group)
	{
		groupRepo.save(group);
		System.out.println("GRP: " + group.getId());
	}
	
	public List<GroupEntity> getGuideNotAllocatedGroup()
	{
		return groupRepo.getGroupEntityNoGuide();
	}

	public List<GroupEntity> getGroups() {
		return groupRepo.findAll();
	}

	public GroupEntity getGroup(String id) {
		return groupRepo.getByGroupId(id);
	}
	
	public GroupEntity getGroupByGroupId(String groupid) {
		return groupRepo.getByGroupId(groupid);
	}

	public List<GroupEntity> getAllocatedGroups() {
		List<GroupEntity> groups = groupRepo.getAllocatedGroups();
		return groups;
	}

	public List<GroupEntity> getNotAllocatedGroups() {
		List<GroupEntity> groups = groupRepo.getNotAllocatedGroups();
		return groups;
	}

	public void assignGuide(String groupid, long guideid) {
		
		Optional<Guide> guide = guideService.getGuideById(guideid);
		GroupEntity group = getGroup(groupid);
		group.guide = guide.get();
		groupRepo.save(group);
	}

	public List<GroupEntity> getGPendingGroups(Guide guide) {
	    return groupRepo.findByGuideAndProjectDefinitionStatus(guide, "gpending");
	}

	
	public long adminPendingProjectDefinitionCount() {
	    return groupRepo.countByProjectDefinitionStatus("gapproved");
	}


	public List<GroupEntity> getAPendingGroups() {
		return groupRepo.findByProjectDefinitionStatus("gapproved");
	}
	
	public long countGPendingGroups() {
	    return groupRepo.countByProjectDefinitionStatus("gpending");
	}

	public List<GroupEntity> getGPendingFinalReports(Guide guide) {
		return groupRepo.findByGuideAndFinalReportStatus(guide, "gpending");
	}

	public List<GroupEntity> getAPendingFinalReports() {
		return groupRepo.findByFinalReportStatus("gapproved");
	}
}