package com.xemsdoom.dt.modules;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.XemDragon;
import com.xemsdoom.dt.commands.CommandHandlers;
import com.xemsdoom.dt.economy.EconomyHandler;
import com.xemsdoom.dt.spout.music.MusicHandler;

/**
 * Copyright (C) 2011-2012 Moser Luca/Philipp Wagner
 * moser.luca@gmail.com/mail@phiwa.eu
 * 
 * This file is part of DragonTravel.
 * 
 * DragonTravel is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * DragonTravel is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Foobar. If not, see <http://www.gnu.org/licenses/>.
 */
public class Travels {

	public static ArrayList<String> togglers = new ArrayList<String>();
	private static ChatColor red = ChatColor.RED;
	private static ChatColor white = ChatColor.WHITE;

	/**
	 * Spawns a XemDragon and mounts the player on it
	 * 
	 * @param player
	 */
	public static boolean mountDragon(Player player) {

		if (DragonTravelMain.config.getBoolean("UseStation"))
			if (!(Stations.checkStation(player)))
				return false;

		if (DragonTravelMain.TravelInformation.containsKey(player)) {
			XemDragon dragon = DragonTravelMain.TravelInformation.get(player);
			Entity dra = dragon.getBukkitEntity();
			removePlayerandDragon(dra);
		}

		net.minecraft.server.World notchWorld = ((CraftWorld) player.getWorld()).getHandle();
		XemDragon XemDragon = new XemDragon(player.getLocation(), notchWorld);
		notchWorld.addEntity(XemDragon);
		LivingEntity dragon = (LivingEntity) XemDragon.getBukkitEntity();

		dragon.setPassenger(player);
		DragonTravelMain.XemDragonRemoval.put(XemDragon, XemDragon);
		DragonTravelMain.TravelInformation.put(player, XemDragon);
		CommandHandlers.dtpCredit(player);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("MountSuccessful")));
		MusicHandler.playEpicSound(player);

		return true;
	}

	/**
	 * Dismounts the player from a dragon if mounted on one.
	 * 
	 * @param player
	 *            entity which gets dismounted by executing /dt dismount
	 */
	public static void dismountDragon(Player player) {

		if (!DragonTravelMain.TravelInformation.containsKey(player)) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DismountNotMounted")));
			return;
		}

		XemDragon dragon = DragonTravelMain.TravelInformation.get(player);

		Entity dra = dragon.getBukkitEntity();
		removePlayerandDragon(dra);
		CommandHandlers.dtpCredit(player);

		Location clone = player.getLocation().clone();
		clone.setY(126);
		for (;;) {
			for (int offset = 0; clone.getBlock().isEmpty() && clone.getY() != 0; offset++) {
				clone.setY(126 - offset);
			}
			if (clone.getY() == 0) {
				clone.setY(126);
				clone.setX(clone.getX() + 1);
			} else {
				break;
			}
		}

		clone.setY(clone.getY() + 2);
		player.teleport(clone);

		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DismountSuccessful")));
	}

	/**
	 * Removes the player of the HashMap "TravelInformation" and removes the
	 * dragon out of the world, also dismounts the player.
	 * 
	 * @param entity
	 *            the dragon entity used to do stuff
	 */
	public static void removePlayerandDragon(Entity entity) {
		Player player = (Player) entity.getPassenger();
		MusicHandler.stopEpicSound(player);
		DragonTravelMain.TravelInformation.remove(player);

		Location clone = player.getLocation().clone();
		clone.setY(126);
		for (;;) {
			for (int offset = 0; clone.getBlock().isEmpty() && clone.getY() != 0; offset++) {
				clone.setY(126 - offset);
			}
			if (clone.getY() == 0) {
				clone.setY(126);
				clone.setX(clone.getX() + 1);
			} else {
				break;
			}
		}

		clone.setY(clone.getY() + 2);
		player.teleport(clone);

		entity.eject();
		entity.remove();

	}

	/**
	 * Removes all enderdragons in the same world as the command executor, which
	 * do not have players as passengers
	 */
	public static void removeDragons(Player player) {
		int amount = player.getWorld().getEntities().size();
		int passed = 0;
		int counter = 0;

		for (Entity entity : player.getWorld().getEntities()) {
			counter++;
			if (entity instanceof EnderDragon) {
				if (!(entity.getPassenger() instanceof Player)) {
					entity.remove();
					passed++;
				}
			}
			if (counter == amount) {
				CommandHandlers.dtpCredit(player);
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("RemoveDragons1")) + " " + passed + " " + MessagesLoader.replaceColors(DragonTravelMain.messages.getString("RemoveDragons2")));
			}
		}
	}

	/**
	 * Removes all enderdragons in a given world, which do not have players as
	 * passengers. Reports back to log
	 */
	public static void removeDragons(World world) {
		if (world == null)
			return;

		int amount = world.getEntities().size();
		int passed = 0;
		int counter = 0;

		for (Entity entity : world.getEntities()) {
			counter++;
			if (entity instanceof EnderDragon) {
				if (!(entity.getPassenger() instanceof Player)) {
					entity.remove();
					passed++;
				}
			}

			if (counter == amount) {
				DragonTravelMain.log.info(String.format("[DragonTravel] Removed %s dragon(s)", passed));
			}
		}
	}

	// Traveling

	/**
	 * Travels the mounted player to a destination passed in the command as an
	 * arg[1]. If the destination is not available or in a other world, the
	 * players gets message to say so. Also charges player if "Economy" is set
	 * to true.
	 * 
	 * @param sender
	 *            the sender of this command, which could travel to a
	 *            destination
	 * @param name
	 *            the destination name passed in the command as arg[1]
	 */
	public static void travelDestination(Player player, String name) {

		if (!name.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
			if (!DragonTravelMain.dbd.hasIndex(name)) {
				CommandHandlers.dtpCredit(player);
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationTravelDoesNotExist")));
				return;
			}

			if (!player.getWorld().toString().equalsIgnoreCase(DragonTravelMain.dbd.getString(name, "world"))) {
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationTravelDifferentWorld")));
				return;
			}
		}

		if (!(player.hasPermission("dt.nocost")))
			if (!EconomyHandler.chargePlayer(player))
				return;

		if (!mountDragon(player))
			return;

		if (!DragonTravelMain.TravelInformation.containsKey(player))
			return;

		XemDragon dragon = DragonTravelMain.TravelInformation.get(player);

		if (dragon == null)
			return;

		double x;
		double y;
		double z;

		if (name.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
			int minX = DragonTravelMain.config.getInt("X-Axis.MinX");
			int maxX = DragonTravelMain.config.getInt("X-Axis.MaxX");
			int minZ = DragonTravelMain.config.getInt("Z-Axis.MinZ");
			int maxZ = DragonTravelMain.config.getInt("Z-Axis.MaxZ");
			x = minX + (Math.random() * (maxX - 1));
			z = minZ + (Math.random() * (maxZ - 1));
			Location randomLoc = new Location(player.getWorld(), x, 10, z);
			y = randomLoc.getWorld().getHighestBlockAt(randomLoc).getY();
		} else {
			x = DragonTravelMain.dbd.getDouble(name, "x");
			y = DragonTravelMain.dbd.getDouble(name, "y");
			z = DragonTravelMain.dbd.getDouble(name, "z");
		}

		Location loca = new Location(player.getWorld(), x, y, z);
		dragon.startTravel(loca);

		if (name.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationTravelRandom")));
		} else {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationTravelTo")) + " " + name);
		}

	}

	/**
	 * Travels the mounted player to a destination passed in the command as an
	 * arg[1]. If the destination is not available or in a other world, the
	 * players gets message to say so. Also charges player if "Economy" is set
	 * to true. FOR SIGNS!
	 * 
	 * @param sender
	 *            the sender of this command, which could travel to a
	 *            destination
	 * @param name
	 *            the destination name passed in the command as arg[1]
	 */

	public static void travelDestinationSigns(Player player, String name) {

		if (!DragonTravelMain.TravelInformation.containsKey(player)) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(red + "You are not mounted on a dragon");
			player.sendMessage(red + "Use" + white + " /dt mount" + red + " to mount a dragon");
			return;
		}

		XemDragon dragon = DragonTravelMain.TravelInformation.get(player);

		if (!name.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
			if (!player.getWorld().toString().equalsIgnoreCase(DragonTravelMain.dbd.getString(name, "world"))) {
				removePlayerandDragon(dragon.getBukkitEntity());
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationTravelDifferentWorld")));
				return;
			}
		}

		if (dragon == null)
			return;

		double x;
		double y;
		double z;

		if (name.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
			int minX = DragonTravelMain.config.getInt("X-Axis.MinX");
			int maxX = DragonTravelMain.config.getInt("X-Axis.MaxX");
			int minZ = DragonTravelMain.config.getInt("Z-Axis.MinZ");
			int maxZ = DragonTravelMain.config.getInt("Z-Axis.MaxZ");
			x = minX + (Math.random() * (maxX - 1));
			z = minZ + (Math.random() * (maxZ - 1));
			Location randomLoc = new Location(player.getWorld(), x, 10, z);
			y = randomLoc.getWorld().getHighestBlockAt(randomLoc).getY();
		} else {
			x = DragonTravelMain.dbd.getDouble(name, "x");
			y = DragonTravelMain.dbd.getDouble(name, "y");
			z = DragonTravelMain.dbd.getDouble(name, "z");
		}

		Location loca = new Location(player.getWorld(), x, y, z);
		dragon.startTravel(loca);

		if (name.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationTravelRandom")));
		} else {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationTravelTo")) + " " + name);
		}
	}

	/**
	 * Travels the player to the passed chordinates typed in on the command.
	 * Also charges player if "Economy" is set to true.
	 * 
	 * @param player
	 *            entity which is mounted on the dragon, fyling to the
	 *            coordinates.
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinates
	 */
	public static void travelChord(Player player, double x, double y, double z) {

		if (!(player.hasPermission("dt.nocost")))
			if (!EconomyHandler.chargePlayerCoordsTravel(player))
				return;

		mountDragon(player);

		XemDragon dragon = DragonTravelMain.TravelInformation.get(player);
		if (dragon == null)
			return;

		Location loca = new Location(player.getWorld(), x, y, z);

		dragon.startTravel(loca);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("CoordinatesTravelTo")) + " " + x + ", " + y + ", " + z);
	}

	/**
	 * Travels the player to the player passed on the command. Also charges the
	 * player if "Economy" is set to true. Disallows Traveling to players in
	 * other world.
	 * 
	 * @param player
	 *            entity which travels to the other player
	 * @param name
	 *            the playername which is used to get the exact player in order
	 *            to travel to him/her
	 */
	public static void traveltoPlayer(Player player, String name) {

		if (player.getServer().getPlayer(name) == null) {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("PlayerNotOnline")));
			return;
		}

		Player target = player.getServer().getPlayer(name);

		// Returning if the target-player has ptravel turned off
		if (togglers.contains(target.getName())) {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("PlayerHasPTravelTurnedOff")));
			return;
		}

		String targetname = target.getName();

		if (target.getWorld() != player.getWorld())
			player.sendMessage(targetname + " " + MessagesLoader.replaceColors(DragonTravelMain.messages.getString("PlayerDifferentWorld")));

		if (!(player.hasPermission("dt.nocost")))
			if (!EconomyHandler.chargePlayerTravelPlayer(player))
				return;

		if (!(mountDragon(player)))
			return;

		XemDragon dragon = DragonTravelMain.TravelInformation.get(player);

		if (dragon == null)
			return;

		Location loca = target.getLocation().subtract(0.0D, 5.0D, 0.0D);

		dragon.startTravel(loca);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("PlayerTravelTo")) + " " + name);

	}
}
