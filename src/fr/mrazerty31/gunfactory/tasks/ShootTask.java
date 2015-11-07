package fr.mrazerty31.gunfactory.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.mrazerty31.gunfactory.GunFactory;
import fr.mrazerty31.gunfactory.gun.Gun;

public class ShootTask extends BukkitRunnable {
	private Player player;
	private Gun gun;
	
	public ShootTask(Player player, Gun gun) {
		this.player = player;
		this.gun = gun;
	}
	
	public void run() {}
	
	public void runTaskTimer() {
		this.runTaskTimer(GunFactory.instance, 0L, 1L);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Gun getGun() {
		return gun;
	}
	
	public void setGun(Gun gun) {
		this.gun = gun;
	}
}
