package de.livinglab.dashboard.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Definition {
	private String id, name, packageName, deploymentId, formUrl, diagramUrl;
	private int version;
	private boolean suspended;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packagename) {
		this.packageName = packagename;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getFormUrl() {
		return formUrl;
	}
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
	public String getDiagramUrl() {
		return diagramUrl;
	}
	public void setDiagramUrl(String diagramUrl) {
		this.diagramUrl = diagramUrl;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	@Override
	public String toString() {
		return "Definition [id=" + id + ", name=" + name + ", packagename="
				+ packageName + ", deploymentId=" + deploymentId + ", formUrl="
				+ formUrl + ", diagramUrl=" + diagramUrl + ", version="
				+ version + ", suspended=" + suspended + "]";
	}

}
