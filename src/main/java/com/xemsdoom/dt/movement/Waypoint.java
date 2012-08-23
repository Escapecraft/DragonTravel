package com.xemsdoom.dt.movement;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Defines location out of coordinates x,y,z
 */
public class Waypoint {

	public static HashMap<Block, Block> markers = new HashMap<Block, Block>();
	private double x;
	private double y;
	private double z;
	private Block marker;

	public void setMarker(Player player) {
		marker = player.getLocation().getBlock();
		marker.setType(Material.GLOWSTONE);
		markers.put(marker, marker);
	}

	public void removeMarker() {
		marker.setType(Material.AIR);
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return this.x;
	}

	public void setY(double y) {
		this.y = y - 2;
	}

	public double getY() {
		return this.y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return this.z;
	}

}
