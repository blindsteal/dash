package de.livinglab.dashboard.events;

final public class LoadRequest {

	final private String uri;
	final private ResourceType type;
	final private Class<?> returnType;
	final private boolean refresh;
	
	public enum ResourceType{
		PLAIN_XML, PLAIN_JSON, ALL_XML, BASICAUTH_XML
	}

	public LoadRequest(String uri, ResourceType type, Class<?> returnType, boolean refresh) {
		super();
		this.uri = uri;
		this.type = type;
		this.returnType = returnType;
		this.refresh = refresh;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public String getUri() {
		return uri;
	}

	public ResourceType getType() {
		return type;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	@Override
	public String toString() {
		return "LoadRequest [uri=" + uri + ", type=" + type + ", returnType="
				+ returnType + ", refresh=" + refresh + "]";
	}
}
