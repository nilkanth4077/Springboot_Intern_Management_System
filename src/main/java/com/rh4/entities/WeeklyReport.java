package com.rh4.entities;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weekly_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "weekly_report_id")
	private Long weeklyReportId;

	@ManyToOne
	@JoinColumn(name = "intern_id")
	private Intern intern;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private GroupEntity group;

	@Column(name = "report_submitted_date")
	private Date reportSubmittedDate;

	@Column(name = "deadline")
	private Date deadline;

	@Column(name = "week_no")
	private int weekNo;

	@Lob
	@Column(name = "submitted_pdf", columnDefinition = "BYTEA")
	private byte[] submittedPdf;

	@ManyToOne
	@JoinColumn(name = "guide_id")
	private Guide guide;

	@ManyToOne
	@JoinColumn(name = "replaced_by")
	private MyUser replacedBy;

	private String status;

	public Long getWeeklyReportId() {
		return weeklyReportId;
	}

	public void setWeeklyReportId(Long weeklyReportId) {
		this.weeklyReportId = weeklyReportId;
	}

	public Intern getIntern() {
		return intern;
	}

	public void setIntern(Intern intern) {
		this.intern = intern;
	}

	public GroupEntity getGroup() {
		return group;
	}

	public void setGroup(GroupEntity group) {
		this.group = group;
	}

	public Date getReportSubmittedDate() {
		return reportSubmittedDate;
	}

	public void setReportSubmittedDate(Date reportSubmittedDate) {
		this.reportSubmittedDate = reportSubmittedDate;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public int getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(int weekNo) {
		this.weekNo = weekNo;
	}

	public byte[] getSubmittedPdf() {
		return submittedPdf;
	}

	public void setSubmittedPdf(byte[] submittedPdf) {
		this.submittedPdf = submittedPdf;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
	}

	public MyUser getReplacedBy() {
		return replacedBy;
	}

	public void setReplacedBy(MyUser replacedBy) {
		this.replacedBy = replacedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}