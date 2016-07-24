package org.greensky.platformjump;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
	private org.bukkit.Material platformMaterial;
	private boolean isDebugOn;
	private FileConfiguration config;

	@SuppressWarnings("deprecation")
	public Configuration(FileConfiguration config) {
		this.config = config;

		this.isDebugOn = config.getBoolean("debug");
		this.platformMaterial = org.bukkit.Material.getMaterial(config.getInt("platform.block-id"));
	}

	public org.bukkit.Material getPlatformMaterial() {
		return this.platformMaterial;
	}

	public boolean isDebugOn() {
		return this.isDebugOn;
	}

	@SuppressWarnings("deprecation")
	public void reload() {
		this.isDebugOn = this.config.getBoolean("debug");
		this.platformMaterial = org.bukkit.Material.getMaterial(this.config.getInt("platform.block-id"));
	}

	public void setDebugOn(boolean isDebugOn) {
		this.isDebugOn = isDebugOn;
	}

	public void setPlatformMaterial(org.bukkit.Material platformMaterial) {
		this.platformMaterial = platformMaterial;
	}
}
