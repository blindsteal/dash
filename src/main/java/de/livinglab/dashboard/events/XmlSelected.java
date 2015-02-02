package de.livinglab.dashboard.events;

public final class XmlSelected implements UiEvent{
	private final String nodeString;

	public XmlSelected(String nodeString) {
		super();
		this.nodeString = nodeString;
	}

	public String getNodeString() {
		return nodeString;
	}

	@Override
	public String toString() {
		return "XmlSelected [nodeString=" + nodeString + "]";
	}

}
