package fr.mrazerty31.gunfactory.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.mrazerty31.gunfactory.gun.Gun;

public class PlayerListener implements Listener {
	@EventHandler
	public void rightClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// ItemStack item = e.getItem();
			Player p = e.getPlayer();
			if(Gun.playerGuns.containsKey(p)) {
				Gun.playerGuns.get(p).shoot(p);
			}
		}
	}
}
