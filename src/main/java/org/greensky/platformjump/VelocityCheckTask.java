/**
 * 
 */
package org.greensky.platformjump;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.greensky.platformjump.util.BukkitUtils;

/**
 * @author Henry Hu
 *
 */
public class VelocityCheckTask extends BukkitRunnable {
	private PlatformJump plugin;

	/**
	 * 
	 */
	public VelocityCheckTask(PlatformJump plugin) {
		// TODO Auto-generated constructor stub
		this.plugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (Player eachPlayer : BukkitUtils.getOnlinePlayers()) {
			if (eachPlayer.getVelocity().getY() <= 0 && eachPlayer.getVelocity().getY() > -0.08) {

				if (this.plugin.getLastPlatformMap().containsKey(eachPlayer.getName())) {

					// Sets location to one under where it used to be, without
					// changing the
					// player's own position
					Location platformLocation = eachPlayer.getLocation().clone();
					platformLocation.setY(platformLocation.getY() - 1.0D);

					Block platform = platformLocation.getBlock();

					if (platform.getType() != plugin.getConfiguration().getPlatformMaterial()
							&& platform.getType() != Material.AIR) {
						this.plugin.getLastPlatformMap().get(eachPlayer.getName()).setType(org.bukkit.Material.AIR);
						this.plugin.getLastPlatformMap().remove(eachPlayer.getName());
						if (plugin.getConfiguration().isDebugOn()) {
							// TODO Add codes
							eachPlayer.sendMessage("X: " + eachPlayer.getVelocity().getX());
							eachPlayer.sendMessage("Y: " + eachPlayer.getVelocity().getY());
							eachPlayer.sendMessage("Z: " + eachPlayer.getVelocity().getZ());

						}

					}

				}

			}
		}

	}

}
