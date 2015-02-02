package de.livinglab.dashboard.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Task {
	private String id, processInstanceId, processId, name, assignee, currentState, url, priority;
	private boolean isBlocking, isSignalling;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public boolean isBlocking() {
		return isBlocking;
	}
	public void setBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}
	public boolean isSignalling() {
		return isSignalling;
	}
	public void setSignalling(boolean isSignalling) {
		this.isSignalling = isSignalling;
	}
	@Override
	public String toString() {
		return "Task [id=" + id + ", processInstanceId=" + processInstanceId
				+ ", processId=" + processId + ", name=" + name + ", assignee="
				+ assignee + ", currentState=" + currentState + ", url=" + url
				+ ", priority=" + priority + ", isBlocking=" + isBlocking
				+ ", isSignalling=" + isSignalling + "]";
	}
}
