package fr.mrazerty31.gunfactory.gun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.inventivegames.particle.ParticleEffect;
import fr.mrazerty31.gunfactory.GunFactory;
import fr.mrazerty31.gunfactory.lib.ItemLib;
import fr.mrazerty31.gunfactory.tasks.ShootTask;

public class Gun {
	private static List<Gun> guns = new ArrayList<Gun>();
	public static HashMap<Player, Gun> playerGuns = new HashMap<Player, Gun>();
	private String name;
	private int damage, size, reloadersSize;
	private int[] reloader = new int[2];
	private ItemStack item;
	private Ammo ammo;

	public Gun(String name, int size, int reloaders, int damage, Ammo ammo) {
		this.name = name;
		this.reloader = new int[] {reloaders, size};
		this.size = size;
		this.reloadersSize = reloaders;
		this.damage = damage;
		this.item = ItemLib.addLore(ItemLib.addDisplayName(new ItemStack(Material.STICK, size), "§r" + name), getItemLore());
		this.ammo = ammo;

		guns.add(this);
	}

	public Gun(String name, int size, int reloaders, int damage, Ammo ammo, ItemStack item) {
		this(name, size, reloaders, damage, ammo);
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getReloader() {
		return reloader;
	}

	public void setReloader(int[] reloader) {
		this.reloader = reloader;
	}

	public int getReloaderSize() {
		return size;
	}

	public void setReloaderSize(int size) {
		this.size = size;
	}

	public int getReloadersSize() {
		return reloadersSize;
	}

	public void setReloadersSize(int amount) {
		this.reloadersSize = amount;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getAmmoAmount() {
		return reloader[1];
	}

	public void setAmmoAmount(int amount) {
		this.reloader[1] = amount;
	}

	public int getReloadersAmount() {
		return reloader[0];
	}

	public void setReloadersAmount(int amount) {
		this.reloader[0] = amount;
	}

	public Ammo getAmmo() {
		return ammo;
	}

	public void setAmmo(Ammo ammo) {
		this.ammo = ammo;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public List<String> getItemLore() {
		List<String> lore = new ArrayList<String>();
		lore.add("§r§6Ammos: §l" + getAmmoAmount() + "/" + getReloaderSize());
		lore.add("§r§6Reloaders: §l" + getReloadersAmount() + "/" + getReloadersSize());
		return lore;
	}

	public boolean canShoot() {
		return getAmmoAmount() > 0;
	}

	public void reload(Player p) {
		if(getReloadersAmount() > 0) {
			setReloadersAmount(getReloadersAmount() - 1);
			setAmmoAmount(getReloaderSize());

			ItemMeta iM = p.getItemInHand().getItemMeta();
			iM.setDisplayName("§r" + getName());
			iM.setLore(getItemLore());
			p.getItemInHand().setAmount(getReloaderSize());
			p.getItemInHand().setItemMeta(iM);
			p.updateInventory();
		} else return;
	}

	public void shoot(final Player p) {
		if(canShoot()) {
			final Item bullet = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(getAmmo().getMaterial()));
			bullet.setVelocity(p.getLocation().getDirection().multiply(2.0F));
			new ShootTask(p, this) {
				@Override
				public void run() {
					for(Entity entity : p.getNearbyEntities(25, 25, 25)) {
						if(entity instanceof LivingEntity) {
							if(!entity.equals(p)) {
								if(!bullet.isDead()) {
									if(bullet.getLocation().distance(entity.getLocation()) <= 1.25) {
										if(entity instanceof Player) {
											((Player) entity).damage(getGun().getDamage(), p);
										} else {
											((LivingEntity) entity).damage(getGun().getDamage());
										}
										bullet.remove();
										cancel();
									}
								} else cancel();
							}
						}
					}
				}
			}.runTaskTimer();

			new BukkitRunnable() {
				@Override
				public void run() {
					bullet.remove();
				}
			}.runTaskLater(GunFactory.instance, 80L);
			shootAmmo(p);
			p.getWorld().playSound(p.getEyeLocation(), Sound.EXPLODE, 0.75F, 1.25F);
			try {
				ParticleEffect.EXPLODE.sendToPlayer(p, p.getEyeLocation(), 0.5F, 0F, 0.5F, 0.8F, 2, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if(getReloadersAmount() > 0) {
				p.getWorld().playSound(p.getEyeLocation(), Sound.PISTON_EXTEND, 1.0F, 0.75F);
				new BukkitRunnable() {
					@EventHandler
					public void run() {
						p.getWorld().playSound(p.getEyeLocation(), Sound.PISTON_RETRACT, 1.0F, 0.75F);
						reload(p);

					}
				}.runTaskLater(GunFactory.instance, 10L);
			} else {
				p.getWorld().playSound(p.getEyeLocation(), Sound.FIRE_IGNITE, 1.0F, 1.3F);
				p.sendMessage("§cNot enough ammo !");
			}
		}
	}

	private void shootAmmo(Player player) {
		if(getAmmoAmount() > 0) {
			setAmmoAmount(getAmmoAmount() - 1);
		} else {
			setAmmoAmount(getReloaderSize());
			setReloadersAmount(getReloadersAmount() - 1);
		}

		if(getAmmoAmount() > 0) {
			player.getItemInHand().setAmount(getAmmoAmount());
			ItemMeta iM = player.getItemInHand().getItemMeta();
			iM.setLore(getItemLore());
			player.getItemInHand().setItemMeta(iM);
			player.updateInventory();
		} else {
			ItemMeta iM = player.getItemInHand().getItemMeta();
			iM.setDisplayName("§c" + getName());
			iM.setLore(getItemLore());
			player.getItemInHand().setItemMeta(iM);
			player.updateInventory();
		}
	}

	public static List<Gun> getGunsList() {
		return guns;
	}

	public static void register() {
		Ammo dft = new Ammo("default", Material.COAL_BLOCK);
		new Gun("Revolver", 8, 2, 5, dft);
	}
}
