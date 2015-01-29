package de.livinglab.dashboard.events;

import de.livinglab.dashboard.data.Instance;

public final class InstanceSelected {
	private final Instance instance;

	public InstanceSelected(Instance instance) {
		super();
		this.instance = instance;
	}

	public Instance getInstance() {
		return instance;
	}

	
}
