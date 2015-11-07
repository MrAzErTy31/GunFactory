package fr.mrazerty31.gunfactory;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.mrazerty31.gunfactory.gun.Gun;
import fr.mrazerty31.gunfactory.listener.PlayerListener;

public class GunFactory extends JavaPlugin {
	public static GunFactory instance;

	public void onEnable() {
		instance = this;
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		Gun.register();
	}

	public void onDisable() {
		instance = null;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			final Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("gun")) {
				if(args.length > 0) {
					if(args[0].equalsIgnoreCase("get")) {
						String gunName = args[1];
						for(Gun gun : Gun.getGunsList()) {
							if(gun.getName().toLowerCase().equals(gunName.toLowerCase())) {
								p.getInventory().addItem(gun.getItem());
								Gun.playerGuns.put(p, gun);
								p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
							} else {
								p.sendMessage("§cIl n'existe aucun gun de ce nom ...");
							}
						}
					} else if(args[0].equalsIgnoreCase("reload")) {
						if(Gun.playerGuns.containsKey(p)) {
							final Gun gun = Gun.playerGuns.get(p);
							gun.setReloadersAmount(gun.getReloadersSize() + 1); // gun.reload(p) decreases reloaders amount.
							
							p.getWorld().playSound(p.getEyeLocation(), Sound.PISTON_EXTEND, 1.0F, 0.75F);
							new BukkitRunnable() {
								@EventHandler
								public void run() {
									p.getWorld().playSound(p.getEyeLocation(), Sound.PISTON_RETRACT, 1.0F, 0.75F);
									gun.reload(p);

								}
							}.runTaskLater(GunFactory.instance, 10L);
						} else {
							p.sendMessage("§cYou don't have any gun.");
						}
					} else {
						p.sendMessage("§eCommande en cours de développement.");
					}
				} else {
					p.sendMessage("§eUtilisation: /gun <args>");
				}
			}
		}
		return true;
	}
}
