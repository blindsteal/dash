package de.livinglab.dashboard.events;

import de.livinglab.dashboard.data.Instance;

public final class InstanceSelected implements UiEvent {
	private final Instance instance;

	public InstanceSelected(Instance instance) {
		super();
		this.instance = instance;
	}

	public Instance getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		return "InstanceSelected [instance=" + instance + "]";
	}

	
}
