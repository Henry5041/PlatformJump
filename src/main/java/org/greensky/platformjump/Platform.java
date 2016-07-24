package org.greensky.platformjump;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Platform {
	private Map<String, Block> lastPlatformMap;
	private boolean isDebugOn;
	private Player player;
	private org.bukkit.Material platformMaterial;

	public Platform(Player player) {
		this.player = player;
		this.lastPlatformMap = null;
		setDebugOn(false);
		this.platformMaterial = org.bukkit.Material.GLASS;
	}

	/**
	 * 
	 * @return true if the platform is successfully created
	 */
	public boolean createPlatform() {
		String playerName = this.player.getName();
		this.removeLast();
		Location playerLocation = this.player.getLocation();
		// Sets location to one under where it used to be, without changing the
		// player's own position
		Location platformLocation = playerLocation.clone();
		platformLocation.setY(platformLocation.getY() - 1.0D);

		Block platform = platformLocation.getBlock();

		if (platform.getType() == org.bukkit.Material.AIR) {
			platform.setType(this.platformMaterial);
			if (this.lastPlatformMap != null) {
				this.lastPlatformMap.put(playerName, platform);
			}

			if (this.isDebugOn) {
				this.player.sendMessage("Platform created");
			}
			platformLocation.setY((int) platformLocation.getY());
			// unstuck the player
			this.player.teleport(playerLocation);
			if (this.isDebugOn) {
				this.player.sendMessage("Teleported upon the platform");
			}

			return true;
		}

		return false;
	}

	public Map<String, Block> getLastPlatformMap() {
		return this.lastPlatformMap;
	}

	public org.bukkit.Material getPlatformMaterial() {
		return this.platformMaterial;
	}

	public boolean isDebugOn() {
		return this.isDebugOn;
	}

	public boolean removeLast() {
		String playerName = this.player.getName();
		if ((this.lastPlatformMap != null) && (this.lastPlatformMap.containsKey(playerName))) {
			Block lastPlatform = this.lastPlatformMap.get(playerName);
			if (lastPlatform.getType() == this.platformMaterial) {
				lastPlatform.setType(org.bukkit.Material.AIR);
				// Remove the last platform block
				this.lastPlatformMap.remove(playerName);
				if (this.isDebugOn) {
					this.player.sendMessage("Last platform removed");
				}
				return true;
			}
		}
		return false;
	}

	public void setDebugOn(boolean isDebugOn) {
		this.isDebugOn = isDebugOn;
	}

	public void setLastPlatformMap(Map<String, Block> lastPlatformMap) {
		this.lastPlatformMap = lastPlatformMap;
	}

	public void setPlatformMaterial(org.bukkit.Material platformType) {
		this.platformMaterial = platformType;
	}
}
