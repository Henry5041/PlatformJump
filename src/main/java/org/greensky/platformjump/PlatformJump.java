package org.greensky.platformjump;

import java.util.HashMap;
import java.util.Map;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.greensky.platformjump.util.BukkitUtils;

public class PlatformJump extends org.bukkit.plugin.java.JavaPlugin {
	public Map<String, Boolean> pluginEnabled = new HashMap<String, Boolean>();
	private Map<String, Block> lastPlatformMap = new HashMap<String, Block>();
	private Configuration configuration;

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public Map<String, Block> getLastPlatformMap() {
		return this.lastPlatformMap;
	}

	public boolean getPluginEnabled(String playerName) {
		if (!this.pluginEnabled.containsKey(playerName)) {
			this.pluginEnabled.put(playerName, Boolean.valueOf(true));
		}
		return this.pluginEnabled.get(playerName).booleanValue();
	}

	@Override
	public void onDisable() {

		for (Player eachPlayer : BukkitUtils.getOnlinePlayers()) {
			if (this.lastPlatformMap.containsKey(eachPlayer.getName())) {
				this.lastPlatformMap.get(eachPlayer.getName()).setType(Material.AIR);
			}
		}
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.configuration = new Configuration(getConfig());
		getLogger().info("Debug: " + this.configuration.isDebugOn());
		getLogger().info("Platform Material: " + this.configuration.getPlatformMaterial());
		getCommand("platform").setExecutor(new PlatformJumpCommandExecutor(this));
		getServer().getPluginManager().registerEvents(new PlatformJumpListener(this), this);
		if (getConfig().getBoolean("metrics")) {
			Metrics metrics = new Metrics(this);
		}
		BukkitTask task = new VelocityCheckTask(this).runTaskTimer(this, 20L, 20L);
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setLastPlatformMap(Map<String, Block> lastPlatformMap) {
		this.lastPlatformMap = lastPlatformMap;
	}

	public void togglePluginState(Player player) {
		String playerName = player.getName();
		if (this.pluginEnabled.containsKey(playerName)) {
			if (this.pluginEnabled.get(playerName).booleanValue()) {
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
}
