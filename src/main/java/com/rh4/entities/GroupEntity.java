package com.rh4.entities;

import java.beans.JavaBean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="group_entity")
public class GroupEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name = "group_id")
    private String groupId;
	
	@Column(name = "project_definition")
	private String projectDefinition;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "project_definition_status")
	private String projectDefinitionStatus = "pending";
	
	@Column(name = "final_report_status")
	private String finalReportStatus = "pending";
	
	@Column(name = "project_definition_document")
	private String projectDefinitionDocument;
	
	@Column(name = "final_report")
    private String finalReport;
	
	@Column(name = "domain")
    private String domain;
	
	@JoinColumn(name = "guide_id")
	@ManyToOne
	public Guide guide;
		
   public GroupEntity() {
			super();
			// TODO Auto-generated constructor stub
		}
  	
	public GroupEntity(long id, String groupId, String projectDefinition, String description, String finalReport, String finalReportStatus,
		String projectDefinitionStatus, String projectDefinitionDocument, String domain, Guide guide) {
	super();
	this.id = id;
	this.groupId = groupId;
	this.projectDefinition = projectDefinition;
	this.description = description;
	this.finalReport = finalReport;
	this.finalReport = finalReport;
	this.domain = domain;
	this.projectDefinitionStatus = projectDefinitionStatus;
	this.projectDefinitionDocument = projectDefinitionDocument;
	this.guide = guide;
}
	public String getFinalReportStatus() {
		return finalReportStatus;
	}

	public void setFinalReportStatus(String finalReportStatus) {
		this.finalReportStatus = finalReportStatus;
	}

	public String getFinalReport() {
		return finalReport;
	}

	public void setFinalReport(String finalReport) {
		this.finalReport = finalReport;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProjectDefinition() {
		return projectDefinition;
	}

	public void setProjectDefinition(String projectDefinition) {
		this.projectDefinition = projectDefinition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectDefinitionStatus() {
		return projectDefinitionStatus;
	}

	public void setProjectDefinitionStatus(String projectDefinitionStatus) {
		this.projectDefinitionStatus = projectDefinitionStatus;
	}

	public String getProjectDefinitionDocument() {
		return projectDefinitionDocument;
	}

	public void setProjectDefinitionDocument(String projectDefinitionDocument) {
		this.projectDefinitionDocument = projectDefinitionDocument;
	}
	
}