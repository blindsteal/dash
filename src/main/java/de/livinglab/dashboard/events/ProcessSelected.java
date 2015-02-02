package de.livinglab.dashboard.events;

import de.livinglab.dashboard.data.Definition;

public final class ProcessSelected implements UiEvent{
	private final Definition definiton;

	public ProcessSelected(Definition definiton) {
		super();
		this.definiton = definiton;
	}

	public Definition getDefiniton() {
		return definiton;
	}

	@Override
	public String toString() {
		return "ProcessSelected [definiton=" + definiton + "]";
	}

	
}
