package org.greensky.platformjump;

import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;

public class PlatformJumpListener implements Listener {
	private Map<String, Block> lastPlatformMap;
	private final PlatformJump plugin;
	private Configuration configuration;

	public PlatformJumpListener(PlatformJump plugin) {
		this.plugin = plugin;

		this.lastPlatformMap = plugin.getLastPlatformMap();
		this.configuration = plugin.getConfiguration();
		if (this.configuration.isDebugOn()) {
			plugin.getLogger().info("Listener Platform Material: " + this.configuration.getPlatformMaterial());
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if ((event.getBlock().getType() == this.configuration.getPlatformMaterial())
				&& (this.lastPlatformMap.containsValue(event.getBlock()))) {
			event.setCancelled(true);

			event.getBlock().setType(Material.AIR);

			if (this.configuration.isDebugOn()) {
				event.getPlayer().sendMessage("Drop cleared");
			}
		}
	}

	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
		for (Block eachBlock : event.getBlocks()) {
			if ((eachBlock.getType() == this.configuration.getPlatformMaterial())
					&& (this.lastPlatformMap.containsValue(eachBlock))) {
				eachBlock.setType(Material.AIR);
				if (this.configuration.isDebugOn()) {
					this.plugin.getLogger().info("Piston extend: " + eachBlock.getType());
				}
			}
		}
	}

	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
		@SuppressWarnings("deprecation")
		Block platform = event.getRetractLocation().getBlock();
		if (platform.getType() == this.configuration.getPlatformMaterial()) {
			if (this.lastPlatformMap.containsValue(platform)) {
				event.setCancelled(true);
				platform.setType(Material.AIR);
				if (this.configuration.isDebugOn()) {
					this.plugin.getLogger().info("Piston retract: " + platform.getType());
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL) {
			org.bukkit.entity.Entity entity = event.getEntity();
			if ((entity instanceof Player)) {
				Player player = (Player) entity;
				if (this.lastPlatformMap.containsKey(player.getName())) {
					Location playerLocation = player.getLocation();

					playerLocation.setY(playerLocation.getY() - 1.0D);
					if (this.configuration.isDebugOn()) {
						player.sendMessage("The height of one block under you is " + playerLocation.getBlock().getY());
						player.sendMessage("The height of your last platform is "
								+ this.lastPlatformMap.get(player.getName()).getY());
					}

					if (playerLocation.getBlock().equals(this.lastPlatformMap.get(player.getName()))) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.getLastPlatformMap().containsKey(player.getName())) {
			Location loc = this.plugin.getLastPlatformMap().get(player.getName()).getLocation();
			loc.setWorld(event.getFrom());
			loc.getBlock().setType(org.bukkit.Material.AIR);
			this.plugin.getLastPlatformMap().remove(player.getName());
		}
		event.getFrom();
	}
	// @EventHandler
	// public void onPlayerVelocityEvent(PlayerVelocityEvent event) {
	// if (event.getVelocity().getY() == 0) {
	// Platform platform = new Platform(event.getPlayer());
	// platform.setLastPlatformMap(this.lastPlatformMap);
	// platform.setPlatformMaterial(this.configuration.getPlatformMaterial());
	// platform.setDebugOn(this.configuration.isDebugOn());
	// if (platform.removeLast()) {
	// if (this.configuration.isDebugOn()) {
	// this.plugin.getLogger().info("Velocity:");
	// this.plugin.getLogger().info("X: " + event.getVelocity().getX());
	// this.plugin.getLogger().info("Y: " + event.getVelocity().getY());
	// this.plugin.getLogger().info("Z: " + event.getVelocity().getZ());
	// }
	//
	// }
	// }
	// }

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();

		String playerName = player.getName();

		if (this.plugin.getPluginEnabled(playerName) && event.isSneaking() && !player.isFlying()) {

			if (player.hasPermission("platform.create")) {
				int platformMaxHeight = this.plugin.getConfig().getInt("platform.max-height");
				if (player.getLocation().getY() - 1.0D <= platformMaxHeight) {
					Platform platform = new Platform(player);
					platform.setLastPlatformMap(this.lastPlatformMap);
					platform.setPlatformMaterial(this.configuration.getPlatformMaterial());
					platform.setDebugOn(this.configuration.isDebugOn());
					if (platform.createPlatform()) {
						player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
						float exhaustionAdd = (float) this.plugin.getConfig().getDouble("exhaustion");
						player.setExhaustion(player.getExhaustion() + exhaustionAdd);
						if (this.configuration.isDebugOn()) {
							player.sendMessage("Current exhaustion: " + player.getExhaustion());
						}
					} else {
						this.lastPlatformMap.remove(playerName);
					}
				} else {
					player.sendMessage(this.plugin.getConfig().getString("message.too-high"));
				}

				int jumpEffectTime = this.plugin.getConfig().getInt("jump-effect.time");
				if (this.configuration.isDebugOn()) {
					player.sendMessage("time:" + jumpEffectTime);
				}
				int jumpEffectLevel = this.plugin.getConfig().getInt("jump-effect.level");
				if (this.configuration.isDebugOn()) {
					player.sendMessage("level:" + jumpEffectLevel);
				}
				PotionEffect jumpEffect = new PotionEffect(org.bukkit.potion.PotionEffectType.JUMP, jumpEffectTime,
						jumpEffectLevel);

				player.addPotionEffect(jumpEffect, true);
			}

		}

	}
}
