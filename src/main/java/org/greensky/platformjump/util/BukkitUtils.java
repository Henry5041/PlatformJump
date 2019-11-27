package org.greensky.platformjump.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BukkitUtils {
	private BukkitUtils() {
	}

	public static List<Player> getOnlinePlayers() {
		List<Player> players = new ArrayList<>();
		for (World world : Bukkit.getWorlds()) {
			players.addAll(world.getPlayers());
		}
		return players;
	}
}
