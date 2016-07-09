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

	public boolean createPlatform() {
		String playerName = this.player.getName();
		if ((this.lastPlatformMap != null) && (this.lastPlatformMap.containsKey(playerName))) {
			Block lastPlatform = (Block) this.lastPlatformMap.get(playerName);
			if (lastPlatform.getType() == this.platformMaterial) {
				lastPlatform.setType(org.bukkit.Material.AIR);
				// Remove the last platform block
				this.lastPlatformMap.remove(playerName);
				if (this.isDebugOn) {
					this.player.sendMessage("Last platform removed");
				}
			}
		}

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

	public void setLastPlatformMap(Map<String, Block> lastPlatformMap) {
		this.lastPlatformMap = lastPlatformMap;
	}

	public boolean isDebugOn() {
		return this.isDebugOn;
	}

	public void setDebugOn(boolean isDebugOn) {
		this.isDebugOn = isDebugOn;
	}

	public org.bukkit.Material getPlatformMaterial() {
		return this.platformMaterial;
	}

	public void setPlatformMaterial(org.bukkit.Material platformType) {
		this.platformMaterial = platformType;
	}
}

/*
 * Location: C:\Users\Henry
 * Hu\Documents\plugins\PlatformJump-0.2.0.jar!\org\greensky\platformjump\
 * Platform.class Java compiler version: 7 (51.0) JD-Core Version: 0.7.1
 */