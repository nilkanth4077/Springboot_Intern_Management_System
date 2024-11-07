package com.rh4.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.rh4.entities.GroupEntity;
import com.rh4.entities.WeeklyReport;
import com.rh4.repositories.WeeklyReportRepo;

@Service
public class WeeklyReportService {

	@Autowired
	private WeeklyReportRepo weeklyReportRepo;

	public int getRecentWeekNo(GroupEntity group) {
			List<WeeklyReport> reports = weeklyReportRepo.getRecentWeekNo(group);
	    if (!reports.isEmpty()) {
	        return reports.get(0).getWeekNo() + 1;
	    } else {
	        // Handle the case where no reports are found
	        return 1; // Or any other appropriate value
	    }
	}

	public void addReport(WeeklyReport weeklyReport) {
		weeklyReportRepo.save(weeklyReport);
		
	}

	public List<WeeklyReport> getReportsByGroupId(long id) {
		return weeklyReportRepo.findAllByGroupId(id);
	}
	
	public List<WeeklyReport> getReportsByGuideId(long id) {
		return weeklyReportRepo.findAllByGuideId(id);
	}
	
	public WeeklyReport getReportByWeekNoAndGroupId(int weekNo, GroupEntity group) {
		return weeklyReportRepo.findByWeekNoAndGroup(weekNo,group);
	}

	public List<WeeklyReport> getAllReports() {
		return weeklyReportRepo.findAll();
	}

	public WeeklyReport getReportByInternIdAndWeekNo(String internId, int weekNo) {
		return weeklyReportRepo.findByInternInternIdAndWeekNo(internId, weekNo);
	}
	
}