package de.livinglab.dashboard.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LogisticServiceProvider")
@XmlAccessorType (XmlAccessType.FIELD)
public class LogisticServiceProvider {
	@XmlAttribute
	private String id;

	@XmlElement
	private String Name;

	@XmlElement
	private String mail;

	@XmlElement
	private String Description;

	@XmlElement
	private String IntegrationIP;

	@XmlElement
	private String phone;

	@XmlElement
	private String fax;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getIntegrationIP() {
		return IntegrationIP;
	}

	public void setIntegrationIP(String integrationIP) {
		IntegrationIP = integrationIP;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
}
