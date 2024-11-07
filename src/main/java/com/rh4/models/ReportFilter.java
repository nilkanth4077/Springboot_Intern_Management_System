package com.rh4.models;

import java.sql.Date;


public class ReportFilter {
    private String college;
    private String branch;
    private String guide;
    private String domain;
    private Date startDate;
    private Date endDate;
    private String cancelled;
    private String format;
	public ReportFilter() {
		super();
	}
	public ReportFilter(String college, String branch, String guide, String domain, Date startDate, Date endDate,
			String cancelled, String format) {
		super();
		this.college = college;
		this.branch = branch;
		this.guide = guide;
		this.format = format;
		this.domain = domain;
		this.startDate = startDate;
		this.endDate = endDate;
		this.cancelled = cancelled;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getGuide() {
		return guide;
	}
	public void setGuide(String guide) {
		this.guide = guide;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCancelled() {
		return cancelled;
	}
	public void setCancelled(String cancelled) {
		this.cancelled = cancelled;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	   
}