package org.greensky.platformjump;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.greensky.platformjump.util.BlockMaterial;

public class Configuration {
	private final Material DEFAULT_MATERIAL = Material.GLASS;
	private Material platformMaterial;
	private boolean isDebugOn;
	private FileConfiguration config;

	public Configuration(FileConfiguration config) {
		this.config = config;

		this.isDebugOn = config.getBoolean("debug");
		try {
			this.platformMaterial = BlockMaterial.matchMaterial(config.getInt("platform.block-id")).parseMaterial();
		} catch (NullPointerException e) {
			this.platformMaterial = DEFAULT_MATERIAL;
		}
	}

	public org.bukkit.Material getPlatformMaterial() {
		return this.platformMaterial;
	}

	public boolean isDebugOn() {
		return this.isDebugOn;
	}

	public void reload() {
		this.isDebugOn = this.config.getBoolean("debug");
		try {
			this.platformMaterial = BlockMaterial.matchMaterial(config.getInt("platform.block-id")).parseMaterial();
		} catch (NullPointerException e) {
			this.platformMaterial = DEFAULT_MATERIAL;
		}
	}

	public void setDebugOn(boolean isDebugOn) {
		this.isDebugOn = isDebugOn;
	}

	public void setPlatformMaterial(org.bukkit.Material platformMaterial) {
		this.platformMaterial = platformMaterial;
	}
}
