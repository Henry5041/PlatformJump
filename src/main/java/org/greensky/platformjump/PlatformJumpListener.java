package org.greensky.platformjump;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlatformJumpListener implements Listener {
	private Map<String, Block> lastPlatformMap;
	private final PlatformJump plugin;
	private boolean isDebugOn;

	public PlatformJumpListener(PlatformJump plugin) {
		this.plugin = plugin; // Store the plug-in in situations where you need
		// it.
		lastPlatformMap = plugin.getLastPlatformMap();
		isDebugOn = plugin.getConfig().getBoolean("debug");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockDispenseEvent(BlockBreakEvent event) {
		isDebugOn = plugin.getConfig().getBoolean("debug");
		if (event.getBlock().getType() == Material.GLASS) {
			if (lastPlatformMap.containsValue(event.getBlock())) {
				// Cancel the break event to avoid drop
				event.setCancelled(true);

				event.getBlock().setType(Material.AIR);

				if (isDebugOn) {
					event.getPlayer().sendMessage("Drop cleared");
				}
			}

		}

	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Block lastPlatform = lastPlatformMap.get(event.getPlayer().getName());
		// Check the Material of the platform block
		if (lastPlatform.getType() == Material.GLASS) {
			lastPlatform.setType(Material.AIR);
			lastPlatformMap.remove(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		isDebugOn = plugin.getConfig().getBoolean("debug");
		Player player = event.getPlayer();
		// Check if player has permission.
		String playerName = player.getName();
		if (plugin.getPluginEnabled(playerName)) {
			// Check if player is sneaking
			if (event.isSneaking()) {
				// Check if player is flying
				if (!player.isFlying()) {
					// Check if player has permission
					if (player.hasPermission("platform.create")) {
						int platformMaxHeight = plugin.getConfig().getInt(
								"platform.max-height");
						if (player.getLocation().getY() - 1 <= platformMaxHeight) {
							Platform platform = new Platform(player);
							platform.setLastPlatformMap(lastPlatformMap);
							platform.setDebugOn(isDebugOn);
							if (platform.createPlatform()) {
								// Play particle to all the players
								for (Player allPlayer : Bukkit.getServer()
										.getOnlinePlayers()) {
									allPlayer.playEffect(player.getLocation(),
											Effect.ENDER_SIGNAL, null);
								}
								float exhaustionAdd = (float) plugin
										.getConfig().getDouble("food-cost");
								player.setExhaustion(player.getExhaustion()
										+ exhaustionAdd);
								if (isDebugOn) {
									player.sendMessage("Current exhaustion: "
											+ player.getExhaustion());
								}
							}

						} else {
							player.sendMessage("can't create platform in this height");

						}

						int jumpEffectTime = plugin.getConfig().getInt(
								"jump-effect.time");
						if (isDebugOn) {
							player.sendMessage("time:" + jumpEffectTime);
						}
						int jumpEffectLevel = plugin.getConfig().getInt(
								"jump-effect.level");
						if (isDebugOn) {
							player.sendMessage("level:" + jumpEffectLevel);
						}
						PotionEffect jumpEffect = new PotionEffect(
								PotionEffectType.JUMP, jumpEffectTime,
								jumpEffectLevel);
						// add the jump effect to player
						player.addPotionEffect(jumpEffect, true);

					}
				}
			}
		}

	}

}
