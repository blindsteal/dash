package de.livinglab.dashboard.events;

import java.util.List;

import de.livinglab.dashboard.data.Location;
import de.livinglab.dashboard.data.LogisticServiceProvider;

public final class LspSelected implements UiEvent {
	private final LogisticServiceProvider lsp;
	private final List<Location> locations;

	public LspSelected(LogisticServiceProvider lsp, List<Location> locations) {
		super();
		this.lsp = lsp;
		this.locations = locations;
	}

	public LogisticServiceProvider getLsp() {
		return lsp;
	}

	@Override
	public String toString() {
		return "LspSelected [lsp=" + lsp + "]";
	}

	public List<Location> getLocations() {
		return locations;
	}

	
}
