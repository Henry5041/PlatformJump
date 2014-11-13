package org.greensky.platformjump;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Platform {
	private Map<String, Block> lastPlatformMap;
	private boolean isDebugOn;
	private Player player;
	private int platformType;

	public Platform(Player player) {
		this.player = player;
		setDebugOn(false);
	}

	public boolean createPlatform() {
		String playerName = player.getName();
		if (!(lastPlatformMap == null)) {
			if (lastPlatformMap.containsKey(playerName)) {
				// Sets location for last platform.
				Block lastPlatform = lastPlatformMap.get(playerName);
				if (lastPlatform.getType() == Material.GLASS) {
					// Removes the platform.
					lastPlatform.setType(Material.AIR);
					lastPlatformMap.remove(playerName);
					if (isDebugOn) {
						player.sendMessage("Last platform removed");
					}
				}

			}
		}

		// Get the player's location.
		Location playerLocation = player.getLocation();
		// Sets location to one under where it used to be. Note
		// that
		// doesn't change the player's position.
		Location platformLocation = playerLocation.clone();
		platformLocation.setY(platformLocation.getY() - 1);
		// Gets the block at the new location.
		Block platform = platformLocation.getBlock();

		if (platform.getType() == Material.AIR) {
			// Sets the block to glass.
			platform.setType(Material.GLASS);
			if (!(lastPlatformMap == null)) {
				lastPlatformMap.put(playerName, platform);

			}
			if (isDebugOn) {
				player.sendMessage("Platform created");
			}
			platformLocation.setY((int) (platformLocation.getY()));
			// Teleport the player upon the platform
			player.teleport(playerLocation);
			if (isDebugOn) {
				player.sendMessage("Teleported upon the platform");
			}
			// Return true if platform created
			return true;
		}
		// Return false if platform isn't created
		return false;
	}

	public Map<String, Block> getLastPlatformMap() {
		return lastPlatformMap;
	}

	public void setLastPlatformMap(Map<String, Block> lastPlatformMap) {
		this.lastPlatformMap = lastPlatformMap;
	}

	public boolean isDebugOn() {
		return isDebugOn;
	}

	public void setDebugOn(boolean isDebugOn) {
		this.isDebugOn = isDebugOn;
	}

	public int getPlatformType() {
		return platformType;
	}

	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}

}
