package com.rh4.models;

public class ProjectDefinition {
    private String projectDefinition;
    private String description;
    private String projectDefinitionDocument;
	public ProjectDefinition() {
		super();
	}
	
	public ProjectDefinition(String projectDefinition, String description, String projectDefinitionDocument) {
		super();
		this.projectDefinition = projectDefinition;
		this.description = description;
		this.projectDefinitionDocument = projectDefinitionDocument;
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
	public String getProjectDefinitionDocument() {
		return projectDefinitionDocument;
	}
	public void setProjectDefinitionDocument(String projectDefinitionDocument) {
		this.projectDefinitionDocument = projectDefinitionDocument;
	}

    
}
