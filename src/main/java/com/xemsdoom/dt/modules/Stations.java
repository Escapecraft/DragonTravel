package com.xemsdoom.dt.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.commands.CommandHandlers;
import com.xemsdoom.mexdb.exception.EmptyIndexException;
import com.xemsdoom.mexdb.system.Entry;

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
public class Stations {

	/**
	 * Stores a new destination in the database.
	 * 
	 * @param player
	 *            the player setting the destination whose location is stored.
	 * @param name
	 *            the name of the destination to set.
	 */
	public static void setDestination(Player player, String name) {

		if (DragonTravelMain.dbd.hasIndex(name)) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationCreatingAlreadyExists")));
			return;
		}

		if (name.length() > 15) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationCreatingNameTooLong")));
			return;
		}

		Entry entry = null;

		try {
			entry = new Entry(name);
		} catch (EmptyIndexException e) {
			e.printStackTrace();
		}

		Location location = player.getLocation();

		entry.addValue("world", player.getWorld().toString());
		entry.addValue("x", location.getX());
		entry.addValue("y", location.getY() - 6);
		entry.addValue("z", location.getZ());
		entry.addValue("name", name);

		if (DragonTravelMain.config.getBoolean("UseStatDestMarker")) {
			Block b = location.getBlock().getRelative(BlockFace.DOWN);
			b.setType(Material.GLOWSTONE);
		}

		DragonTravelMain.dbd.addEntry(entry);
		DragonTravelMain.dbd.push();

		CommandHandlers.dtpCredit(player);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationCreatingSuccessful")) + " " + name);
	}

	/**
	 * Stores a new station in the database.
	 * 
	 * @param player
	 *            the player setting the station whose location is stored.
	 * @param name
	 *            the name of the station to set.
	 */
	public static void setStation(Player player, String name) {

		if (DragonTravelMain.dbs.hasIndex(name)) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("StationCreatingAlreadyExists")));
			return;
		}

		if (name.length() > 15) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("StationCreatingNameTooLong")));
			return;
		}

		Entry entry = null;

		try {
			entry = new Entry(name);
		} catch (EmptyIndexException e) {
			e.printStackTrace();
		}
		
		Location location = player.getLocation();

		entry.addValue("world", player.getWorld().toString());
		entry.addValue("x", location.getX());
		entry.addValue("y", location.getY());
		entry.addValue("z", location.getZ());
		entry.addValue("name", name);

		if (DragonTravelMain.config.getBoolean("UseStatDestMarker")) {
			Block b = location.getBlock().getRelative(BlockFace.DOWN);
			b.setType(Material.GLOWSTONE);
		}

		DragonTravelMain.dbs.addEntry(entry);
		DragonTravelMain.dbs.push();

		CommandHandlers.dtpCredit(player);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("StationCreatingSuccessful")) + " " + name);
	}

	/**
	 * Removes a given destination out of Destinations.mini/dbd
	 * 
	 * @param sender
	 *            executor of the method
	 * @param name
	 *            the destination name
	 */
	public static void removeDestination(CommandSender sender, String name) {

		if (!DragonTravelMain.dbd.hasIndex(name)) {
			CommandHandlers.dtCredit(sender);
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationRemovingDoesNotExist")));
			return;
		}

		CommandHandlers.dtCredit(sender);
		sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationRemovingSuccessful")) + " " + name);
		DragonTravelMain.dbd.removeEntry(name);
		DragonTravelMain.dbd.push();
	}

	/**
	 * Removes a given station out of the Stations.mini/dbs
	 * 
	 * @param sender
	 *            executor of the method
	 * @param name
	 *            the station name
	 */
	public static void removeStation(CommandSender sender, String name) {

		if (!(DragonTravelMain.dbs.hasIndex(name))) {
			CommandHandlers.dtCredit(sender);
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("StationRemovingDoesNotExist")));
			return;
		}

		CommandHandlers.dtCredit(sender);
		sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("StationRemovingSuccessful")) + " " + name);
		DragonTravelMain.dbs.removeEntry(name);
		DragonTravelMain.dbs.push();
	}

	/**
	 * Checks if the player is on a station. false is not, true if yes.
	 * 
	 * @param player
	 *            entity which gets mounted on the dragon, maybe.
	 */
	public static boolean checkStation(Player player) {

		if (DragonTravelMain.TravelInformation.containsKey(player)) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("AlreadyMounted")));
			return false;
		}

		// Get station amount
		int amount = DragonTravelMain.dbs.getIndices().size();

		// Return if there aren't any stations
		if (amount == 0) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NotAtAStation")));
			return false;
		}

		// Getting world in which the player is
		World world = player.getWorld();
		// Getting player location
		Location locplayer = player.getLocation();
		Vector locvaca = new Location(world, locplayer.getX(), locplayer.getY(), locplayer.getZ()).toVector();

		// Iterate over all stations and check if the player is nearby one
		for (String name : DragonTravelMain.dbs.getIndices()) {
			
			double x = DragonTravelMain.dbs.getDouble(name, "x");
			double y = DragonTravelMain.dbs.getDouble(name, "y");
			double z = DragonTravelMain.dbs.getDouble(name, "z");

			// Check if the current station is in the same world as the player
			if (!world.toString().equalsIgnoreCase(DragonTravelMain.dbs.getString(name, "world"))) {
				if (amount == 1) {
					CommandHandlers.dtpCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NotAtAStation")));
					return false;
				}
				amount--;
				continue;
			}

			// Create vector out of station location
			Vector locvec = new Location(world, x, y, z).toVector();

			// Getting differente between station and player location
			double lenga = locvec.subtract(locvaca).length();

			// Return true if the player is nearby the station
			if (lenga <= DragonTravelMain.config.getDouble("DistancetoStation"))
				return true;
			else {
				if (amount == 1) {
					CommandHandlers.dtpCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NotAtAStation")));
					return false;
				}
				amount--;
			}
		}
		return true;
	}

	/**
	 * Shows the current stations available in the same world as the player
	 * 
	 * @param player
	 */
	public static void showStations(Player player) {

		// Getting player worldname
		StringBuilder a = new StringBuilder();
		String pworld = player.getWorld().toString();

		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("StationShowList")));

		// Adding stations names to builder - Only stations from the same world
		// are added to the builder
		for (String index : DragonTravelMain.dbs.getIndices()) {

			String world = DragonTravelMain.dbs.getString(index, "world");

			if (world.equalsIgnoreCase(pworld))
				a.append(index + ", ");
		}

		// Send station names
		player.sendMessage(a.toString());
	}

	/**
	 * Shows the current stations available in the same world as the player
	 * 
	 * @param player
	 */
	public static void showDestinations(Player player) {
		
		// Getting player worldname
		StringBuilder a = new StringBuilder();
		String pworld = player.getWorld().toString();

		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("DestinationShowList")));
		
		// Adding destinations names to builder - Only destinations from the same world
		// are added to the builder
		for (String index : DragonTravelMain.dbd.getIndices()) {

			String world = DragonTravelMain.dbd.getString(index, "world");

			if (world.equalsIgnoreCase(pworld))
				a.append(index + ", ");
		}

		// Send destination names
		player.sendMessage(a.toString());
	}
}