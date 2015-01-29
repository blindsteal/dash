package de.livinglab.dashboard.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "metadata")
public class Metadata {
	
	private String created;
	private String format;
	private String state;
	private String uuid;
	
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
