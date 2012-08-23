package com.xemsdoom.dt.modules;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
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
public class SignCreating {

	private static double distance = DragonTravelMain.config.getDouble("DistancetoStation");
	static ChatColor gold = ChatColor.GOLD;
	static ChatColor red = ChatColor.RED;

	public static void createEntry(String name, String destination, Player player, Block b, int cost, boolean money) {

		Entry entry = null;

		try {
			entry = new Entry(name.concat(destination));
		} catch (EmptyIndexException e) {
			e.printStackTrace();
		}

		entry.addValue("x", b.getX());
		entry.addValue("y", b.getY());
		entry.addValue("z", b.getZ());
		entry.addValue("dest", destination);

		if (money)
			entry.addValue("cost", String.valueOf(cost));

		entry.addValue("world", b.getWorld().toString());
		DragonTravelMain.signs.addEntry(entry);
		DragonTravelMain.signs.push();

		CommandHandlers.dtpCredit(player);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("SignCreatingSaved")));
	}

	// Creates an economy destination sign
	public static void createDestinationSignEconomy(Block b, SignChangeEvent event, Player player, int cost, String destination) {

		for (String name : DragonTravelMain.dbs.getIndices()) {

			double x = DragonTravelMain.dbs.getDouble(name, "x");
			double y = DragonTravelMain.dbs.getDouble(name, "y");
			double z = DragonTravelMain.dbs.getDouble(name, "z");

			World world = b.getWorld();
			Location bloc = b.getLocation();

			if (!world.toString().equalsIgnoreCase(DragonTravelMain.dbs.getString(name, "world")))
				continue;

			Vector locvec = new Location(world, x, y, z).toVector();
			Vector locveca = new Location(world, bloc.getX(), bloc.getY(), bloc.getZ()).toVector();
			Vector vec = locvec.subtract(locveca);
			double lenga = vec.length();

			if (lenga >= distance)
				continue;

			event.setLine(0, gold + "DragonTravel");
			event.setLine(1, DragonTravelMain.dbs.getString(name, "name").toString());
			event.setLine(2, "To: " + destination);
			event.setLine(3, "Cost: " + String.valueOf(cost));

			createEntry(name, destination, player, b, cost, true);
			break;
		}
	}

	// Creates a destination sign
	public static void createDestinationSign(Block b, SignChangeEvent event, Player player, String destination) {

		for (String name : DragonTravelMain.dbs.getIndices()) {
			double x = DragonTravelMain.dbs.getDouble(name, "x");
			double y = DragonTravelMain.dbs.getDouble(name, "y");
			double z = DragonTravelMain.dbs.getDouble(name, "z");

			World world = b.getWorld();
			Location bloc = b.getLocation();

			if (!world.toString().equalsIgnoreCase(DragonTravelMain.dbs.getString(name, "world")))
				continue;

			Vector locvec = new Location(world, x, y, z).toVector();
			Vector locveca = new Location(world, bloc.getX(), bloc.getY(), bloc.getZ()).toVector();
			Vector vec = locvec.subtract(locveca);
			double lenga = vec.length();

			if (lenga >= distance)
				continue;

			event.setLine(0, gold + "DragonTravel");
			event.setLine(1, DragonTravelMain.dbs.getString(name, "name").toString());

			event.setLine(2, "To: " + destination);

			createEntry(name, destination, player, b, 0, false);
			break;
		}
	}

	// Creates a sign with the specific name of that station
	public static void nameStationSign(Block b, SignChangeEvent event, Player player) {

		for (String name : DragonTravelMain.dbs.getIndices()) {

			double x = DragonTravelMain.dbs.getDouble(name, "x");
			double y = DragonTravelMain.dbs.getDouble(name, "y");
			double z = DragonTravelMain.dbs.getDouble(name, "z");

			World world = b.getWorld();
			Location bloc = b.getLocation();

			if (!world.toString().equalsIgnoreCase(DragonTravelMain.dbs.getString(name, "world")))
				continue;

			Vector locvec = new Location(world, x, y, z).toVector();
			Vector locveca = new Location(world, bloc.getX(), bloc.getY(), bloc.getZ()).toVector();
			Vector vec = locvec.subtract(locveca);
			double lenga = vec.length();

			if (lenga >= distance)
				continue;

			event.setLine(0, gold + "DragonTravel");
			event.setLine(1, DragonTravelMain.dbs.getString(name, "name").toString());
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("SignCreatingCreated")));

			break;
		}
	}

	// Creates a sign with the name of that specific destination
	public static void nameDestinationSign(Block b, SignChangeEvent event, Player player) {

		for (String name : DragonTravelMain.dbd.getIndices()) {

			double x = DragonTravelMain.dbd.getDouble(name, "x");
			double y = DragonTravelMain.dbd.getDouble(name, "y");
			double z = DragonTravelMain.dbd.getDouble(name, "z");

			World world = b.getWorld();

			if (!world.toString().equalsIgnoreCase(DragonTravelMain.dbd.getString(name, "world")))
				continue;

			Location bloc = b.getLocation();
			Vector locvec = new Location(world, x, y + 6, z).toVector();
			Vector locveca = new Location(world, bloc.getX(), bloc.getY(), bloc.getZ()).toVector();
			Vector vec = locvec.subtract(locveca);
			double lenga = vec.length();

			if (lenga >= distance)
				continue;

			event.setLine(0, gold + "DragonTravel");
			event.setLine(1, DragonTravelMain.dbd.getString(name, "name"));
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("SignCreatingCreated")));
			break;
		}
	}

	// Creates a flight sign
	public static void createFlightSign(String name, SignChangeEvent event) {

		event.setLine(0, gold + "DragonTravel");
		event.setLine(1, "Flight");
		event.setLine(2, name);
	}

	// Creates a flight sign with cost
	public static void createFlightSignEconomy(String name, SignChangeEvent event, int cost) {

		event.setLine(0, gold + "DragonTravel");
		event.setLine(1, "Flight");
		event.setLine(2, name);
		event.setLine(3, "Cost: " + cost);
	}
}
