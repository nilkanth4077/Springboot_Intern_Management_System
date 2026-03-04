package com.rh4.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "group_id", nullable = false)
	private String groupId;

	@Column(name = "project_definition")
	private String projectDefinition;

	@Column(name = "description")
	private String description;

	@Column(name = "project_definition_status", nullable = false)
	private String projectDefinitionStatus = "pending";

	@Column(name = "final_report_status", nullable = false)
	private String finalReportStatus = "pending";

	@Lob
	@Column(name = "project_definition_document")
	private byte[] projectDefinitionDocument;

	@Lob
	@Column(name = "final_report")
	private byte[] finalReport;

	@Column(name = "domain")
	private String domain;

	@ManyToOne
	@JoinColumn(name = "guide_id")
	private Guide guide;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getFinalReportStatus() {
		return finalReportStatus;
	}

	public void setFinalReportStatus(String finalReportStatus) {
		this.finalReportStatus = finalReportStatus;
	}

	public byte[] getProjectDefinitionDocument() {
		return projectDefinitionDocument;
	}

	public void setProjectDefinitionDocument(byte[] projectDefinitionDocument) {
		this.projectDefinitionDocument = projectDefinitionDocument;
	}

	public byte[] getFinalReport() {
		return finalReport;
	}

	public void setFinalReport(byte[] finalReport) {
		this.finalReport = finalReport;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
	}
}