package org.greensky.platformjump;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlatformJump extends JavaPlugin  {
	public Map<String, Boolean> pluginEnabled = new HashMap<>();
	private Map<String, Block> lastPlatformMap = new HashMap<String, Block>();

	@Override
	public void onEnable() {
	    this.saveDefaultConfig();
		this.getCommand("platform").setExecutor(new PlatformJumpCommandExecutor(this));
		getServer().getPluginManager().registerEvents(new PlatformJumpListener(this), this);
	}
	public boolean getPluginEnabled(String playerName){
		if (!pluginEnabled.containsKey(playerName)){
			pluginEnabled.put(playerName, true);
		}
		return pluginEnabled.get(playerName);

	}
    public void togglePluginState(Player player) {
        // Notice how we use the player name as the key here,
        // not the player object
        String playerName = player.getName();
        if (pluginEnabled.containsKey(playerName)) {
            if (pluginEnabled.get(playerName)) {
                pluginEnabled.put(playerName, false);
                player.sendMessage("Plugin disabled");
            } else {
                pluginEnabled.put(playerName, true);
                player.sendMessage("Plugin enabled");
            }
        } else {
            pluginEnabled.put(playerName, true); //If you want plugin disabled by default change this value to false.
            player.sendMessage("Plugin enabled");
        }
    }
	public Map<String, Block> getLastPlatformMap() {
		return lastPlatformMap;
	}
	public void setLastPlatformMap(Map<String, Block> lastPlatformMap) {
		this.lastPlatformMap = lastPlatformMap;
	}


}
