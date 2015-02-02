package de.livinglab.dashboard.events;

import de.livinglab.dashboard.data.Location;

public final class LocationSelected implements UiEvent {
	private final Location loc;

	public LocationSelected(Location loc) {
		super();
		this.loc = loc;
	}

	public Location getLoc() {
		return loc;
	}

	@Override
	public String toString() {
		return "LocationSelected [loc=" + loc + "]";
	}

	
}
