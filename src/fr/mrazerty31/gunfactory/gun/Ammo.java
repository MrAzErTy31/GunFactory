package fr.mrazerty31.gunfactory.gun;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class Ammo {
	private static List<Ammo> ammos = new ArrayList<Ammo>();
	
	private String name;
	private Material material;
	
	public Ammo(String name, Material material) {
		this.name = name;
		this.material = material;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public static List<Ammo> getAmmosList() {
		return ammos;
	}
}
