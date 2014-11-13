package org.greensky.platformjump;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlatformJumpCommandExecutor implements CommandExecutor {
	private final PlatformJump plugin;

	public PlatformJumpCommandExecutor(PlatformJump plugin) {
		this.plugin = plugin; // Store the plug-in in situations where you need
								// it.
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length == 0) {
			return false;
		}
		switch (args[0]) {
		case "reload":
			if (sender.hasPermission("platform.command.reload")) {
				plugin.reloadConfig();
				sender.sendMessage("PlatformJump configuration reloaded");
			} else {
				sender.sendMessage("No permission");
			}
			return true;
		case "toggle":
			if (sender instanceof Player) {
				if (sender.hasPermission("platform.command.toggle")) {

					plugin.togglePluginState((Player) sender);
				} else {
					sender.sendMessage("No permission");
				}
			} else {
				sender.sendMessage("You are not a player");
			}

			return true;
		}
		return false;
	}
}
