package com.rh4.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh4.entities.GroupEntity;
import com.rh4.entities.WeeklyReport;

@Repository
public interface WeeklyReportRepo extends JpaRepository<WeeklyReport, Long> {

    @Query("FROM WeeklyReport w WHERE w.group = :group AND w.weekNo = :weekNo")
    WeeklyReport findByGroupAndWeekNo(@Param("group") GroupEntity group, @Param("weekNo") int weekNo);

    @Query("FROM WeeklyReport w WHERE w.group = :group ORDER BY w.reportSubmittedDate DESC")
    List<WeeklyReport> getRecentWeekNo(@Param("group") GroupEntity group);

    List<WeeklyReport> findAllByGroupId(long id);

    @Query("SELECT w FROM WeeklyReport w WHERE w.guide.id = :id")
    List<WeeklyReport> findAllByGuideId(long id);

	WeeklyReport findByWeekNoAndGroup(int weekNo, GroupEntity group);
}
