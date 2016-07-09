package org.greensky.platformjump;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mcstats.Metrics;

public class PlatformJump extends org.bukkit.plugin.java.JavaPlugin {
	public Map<String, Boolean> pluginEnabled = new HashMap<String, Boolean>();
	private Map<String, Block> lastPlatformMap = new HashMap<String, Block>();
	private Configuration configuration;

	public void onEnable() {
		saveDefaultConfig();
		this.configuration = new Configuration(getConfig());
		getLogger().info("Debug: " + this.configuration.isDebugOn());
		getLogger().info("Platform Material: " + this.configuration.getPlatformMaterial());
		getCommand("platform").setExecutor(new PlatformJumpCommandExecutor(this));
		getServer().getPluginManager().registerEvents(new PlatformJumpListener(this), this);
		if (getConfig().getBoolean("metrics")) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				getLogger().info("enable metrics failed");
			}
		}
	}

	public void onDisable() {

		for (Player eachPlayer : org.bukkit.Bukkit.getServer().getOnlinePlayers()) {
			if (this.lastPlatformMap.containsKey(eachPlayer.getName())) {
				((Block) this.lastPlatformMap.get(eachPlayer.getName())).setType(Material.AIR);
			}
		}
	}

	public boolean getPluginEnabled(String playerName) {
		if (!this.pluginEnabled.containsKey(playerName)) {
			this.pluginEnabled.put(playerName, Boolean.valueOf(true));
		}
		return ((Boolean) this.pluginEnabled.get(playerName)).booleanValue();
	}

	public void togglePluginState(Player player) {
		String playerName = player.getName();
		if (this.pluginEnabled.containsKey(playerName)) {
			if (((Boolean) this.pluginEnabled.get(playerName)).booleanValue()) {
				this.pluginEnabled.put(playerName, Boolean.valueOf(false));
				player.sendMessage(getConfig().getString("message.toggle-off"));
			} else {
				this.pluginEnabled.put(playerName, Boolean.valueOf(true));
				player.sendMessage(getConfig().getString("message.toggle-on"));
			}
		} else {
			this.pluginEnabled.put(playerName, Boolean.valueOf(true));

			player.sendMessage(getConfig().getString("message.toggle-on"));
		}
	}

	public Map<String, Block> getLastPlatformMap() {
		return this.lastPlatformMap;
	}

	public void setLastPlatformMap(Map<String, Block> lastPlatformMap) {
		this.lastPlatformMap = lastPlatformMap;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}

/*
 * Location: C:\Users\Henry
 * Hu\Documents\plugins\PlatformJump-0.2.0.jar!\org\greensky\platformjump\
 * PlatformJump.class Java compiler version: 7 (51.0) JD-Core Version: 0.7.1
 */