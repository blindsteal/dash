package de.livinglab.dashboard.events;


final public class LoadComplete {

	final private LoadRequest req;

	public LoadComplete(LoadRequest req) {
		super();
		this.req = req;
	}

	public LoadRequest getReq() {
		return req;
	}

	@Override
	public String toString() {
		return "LoadComplete [req=" + req + "]";
	}

}
