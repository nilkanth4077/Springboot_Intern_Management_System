package com.rh4.entities;

import java.beans.JavaBean;

import jakarta.persistence.*;

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

	@Lob
	@Column(name = "project_definition_document", columnDefinition = "LONGBLOB")
	private byte[] projectDefinitionDocument;

	@Lob
	@Column(name = "final_report", columnDefinition = "LONGBLOB")
    private byte[] finalReport;
	
	@Column(name = "domain")
    private String domain;
	
	@JoinColumn(name = "guide_id")
	@ManyToOne
	public Guide guide;
		
   public GroupEntity() {
			super();
		}
  	
	public GroupEntity(long id, String groupId, String projectDefinition, String description, byte[] finalReport, String finalReportStatus,
		String projectDefinitionStatus, byte[] projectDefinitionDocument, String domain, Guide guide) {
	super();
	this.id = id;
	this.groupId = groupId;
	this.projectDefinition = projectDefinition;
	this.description = description;
	this.finalReport = finalReport;
	this.finalReportStatus = finalReportStatus;
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

	public byte[] getFinalReport() {
		return finalReport;
	}

	public void setFinalReport(byte[] finalReport) {
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

	public byte[] getProjectDefinitionDocument() {
		return projectDefinitionDocument;
	}

	public void setProjectDefinitionDocument(byte[] projectDefinitionDocument) {
		this.projectDefinitionDocument = projectDefinitionDocument;
	}
}