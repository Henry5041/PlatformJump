package org.greensky.platformjump;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlatformJumpCommandExecutor implements org.bukkit.command.CommandExecutor {
	private final PlatformJump plugin;
	private Configuration configuration;

	public PlatformJumpCommandExecutor(PlatformJump plugin) {
		this.plugin = plugin;

		this.configuration = plugin.getConfiguration();
	}

	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (args.length == 0)
			return false;
		String str = args[0].toLowerCase();
		switch ((str)) {
		case "reload":
			if (sender.hasPermission("platform.command.reload")) {
				for (Player eachPlayer : org.bukkit.Bukkit.getServer().getOnlinePlayers()) {

					if (this.plugin.getLastPlatformMap().containsKey(eachPlayer.getName())) {
						((Block) this.plugin.getLastPlatformMap().get(eachPlayer.getName()))
								.setType(org.bukkit.Material.AIR);
						this.plugin.getLastPlatformMap().remove(eachPlayer.getName());
					}
				}

				this.plugin.reloadConfig();
				this.configuration.reload();
				this.plugin.getLogger().info("Platform Material: " + this.configuration.getPlatformMaterial());
				sender.sendMessage(this.plugin.getConfig().getString("message.reloaded"));
			} else {
				sender.sendMessage(this.plugin.getConfig().getString("message.no-permission"));
			}
			return true;
		case "remove":
			if (args.length == 1) {
				if ((sender instanceof Player)) {
					if (sender.hasPermission("platform.command.remove.self")) {
						Player player = (Player) sender;

						if (this.plugin.getLastPlatformMap().containsKey(player.getName())) {
							((Block) this.plugin.getLastPlatformMap().get(player.getName()))
									.setType(org.bukkit.Material.AIR);
							this.plugin.getLastPlatformMap().remove(player.getName());
							sender.sendMessage(this.plugin.getConfig().getString("message.removed"));
						}
					} else {
						sender.sendMessage(this.plugin.getConfig().getString("message.no-permission"));
					}
				} else {
					sender.sendMessage(this.plugin.getConfig().getString("message.not-player"));
				}
				return true;
			}
			if (sender.hasPermission("platform.command.remove.others")) {
				@SuppressWarnings("deprecation")
				Player target = sender.getServer().getPlayer(args[1]);

				if (target == null) {
					sender.sendMessage(args[1] + this.plugin.getConfig().getString("message.not-available"));
					return true;
				}
				if (this.plugin.getLastPlatformMap().containsKey(target.getName())) {
					((Block) this.plugin.getLastPlatformMap().get(target.getName())).setType(org.bukkit.Material.AIR);
					this.plugin.getLastPlatformMap().remove(target.getName());
					sender.sendMessage(this.plugin.getConfig().getString("message.removed"));
				}
				return true;
			}
			sender.sendMessage(this.plugin.getConfig().getString("message.no-permission"));
			break;
		case "toggle":
			if ((sender instanceof Player)) {
				if (sender.hasPermission("platform.command.toggle")) {
					this.plugin.togglePluginState((Player) sender);
				} else {
					sender.sendMessage(this.plugin.getConfig().getString("message.no-permission"));
				}
			} else {
				sender.sendMessage(this.plugin.getConfig().getString("message.not-player"));
			}

			return true;
		}
		return false;
	}
}

/*
 * Location: C:\Users\Henry
 * Hu\Documents\plugins\PlatformJump-0.2.0.jar!\org\greensky\platformjump\
 * PlatformJumpCommandExecutor.class Java compiler version: 7 (51.0) JD-Core
 * Version: 0.7.1
 */