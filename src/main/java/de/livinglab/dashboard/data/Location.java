package de.livinglab.dashboard.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "location")
@XmlAccessorType (XmlAccessType.FIELD)
public class Location {
	
	@XmlAttribute
	private String latitude;
	
	@XmlAttribute
	private String longitude;
	
	@XmlElement
	private String name;
	@XmlElement
	private String postCode;
	@XmlElement
	private String city;
	@XmlElement
	private String street;
	@XmlElement
	private String streetnumber;
	@XmlElement
	private String phone;
	@XmlElement
	private String mail;
	@XmlElement
	private String fax;
	@XmlElement
	private boolean transportCapability;
	@XmlElement
	private boolean warehouseCapability;
	@XmlElement
	private boolean packagingCapability;
	@XmlElement
	private boolean forwarderCapability;
	@XmlElement
	private boolean valueAddedCapability;
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetnumber() {
		return streetnumber;
	}
	public void setStreetnumber(String streetnumber) {
		this.streetnumber = streetnumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public boolean isTransportCapability() {
		return transportCapability;
	}
	public void setTransportCapability(boolean transportCapability) {
		this.transportCapability = transportCapability;
	}
	public boolean isWarehouseCapability() {
		return warehouseCapability;
	}
	public void setWarehouseCapability(boolean warehouseCapability) {
		this.warehouseCapability = warehouseCapability;
	}
	public boolean isPackagingCapability() {
		return packagingCapability;
	}
	public void setPackagingCapability(boolean packagingCapability) {
		this.packagingCapability = packagingCapability;
	}
	public boolean isForwarderCapability() {
		return forwarderCapability;
	}
	public void setForwarderCapability(boolean forwarderCapability) {
		this.forwarderCapability = forwarderCapability;
	}
	public boolean isValueAddedCapability() {
		return valueAddedCapability;
	}
	public void setValueAddedCapability(boolean valueAddedCapability) {
		this.valueAddedCapability = valueAddedCapability;
	}
	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", longitude=" + longitude
				+ ", name=" + name + ", postCode=" + postCode + ", city="
				+ city + ", street=" + street + ", streetnumber="
				+ streetnumber + ", phone=" + phone + ", mail=" + mail
				+ ", fax=" + fax + ", transportCapability="
				+ transportCapability + ", warehouseCapability="
				+ warehouseCapability + ", packagingCapability="
				+ packagingCapability + ", forwarderCapability="
				+ forwarderCapability + ", valueAddedCapability="
				+ valueAddedCapability + "]";
	}


}
