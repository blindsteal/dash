package de.livinglab.dashboard.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "asset")
public class Asset {
	private String author;

	private String binaryLink;
	
	private String refLink;
	
	private String sourceLink;

	private String title;

	private String description;

	private String published;
	
	private String binaryContentAttachmentFileName;
	
	private Metadata metadata;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBinaryLink() {
		return binaryLink;
	}

	public void setBinaryLink(String binaryLink) {
		this.binaryLink = binaryLink;
	}

	public String getRefLink() {
		return refLink;
	}

	public void setRefLink(String refLink) {
		this.refLink = refLink;
	}

	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getBinaryContentAttachmentFileName() {
		return binaryContentAttachmentFileName;
	}

	public void setBinaryContentAttachmentFileName(
			String binaryContentAttachmentFileName) {
		this.binaryContentAttachmentFileName = binaryContentAttachmentFileName;
	}

}
