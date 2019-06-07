package com.github.kalvisan.objects;

import org.bukkit.Location;

public final class WaypointClass {
	
	private String warpName;
	private Location warpLocation;
	private Location warpEndLocation;
	private String id;
	
	public WaypointClass(String id, String name, Location warpLocation, Location endLocation) {
		this.setWarpName(name);
		this.setWarpEndLocation(endLocation);
		this.setId(id);
		this.setWarpLocation(warpLocation);
	}

	public String getWarpName() {
		return warpName;
	}

	public void setWarpName(String warpName) {
		this.warpName = warpName.replace("_", " ");
	}

	public Location getWarpEndLocation() {
		return warpEndLocation;
	}

	public void setWarpEndLocation(Location warpEndLocation) {
		this.warpEndLocation = warpEndLocation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location getWarpLocation() {
		return warpLocation;
	}

	public void setWarpLocation(Location warpLocation) {
		this.warpLocation = warpLocation;
	}
}
