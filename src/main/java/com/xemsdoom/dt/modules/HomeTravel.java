package com.xemsdoom.dt.modules;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.XemDragon;
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
public class HomeTravel {

	/**
	 * Sets a new home for the player, which he can travels to
	 */
	public static void setHome(Player player) {

		Entry entry = null;

		try {
			entry = new Entry(player.getName().toLowerCase());
		} catch (EmptyIndexException e) {
			e.printStackTrace();
		}

		Location location = player.getLocation();

		entry.addValue("world", player.getWorld().toString());
		entry.addValue("x", location.getX());
		entry.addValue("y", location.getY() - 6);
		entry.addValue("z", location.getZ());

		DragonTravelMain.players.addEntry(entry);
		DragonTravelMain.players.push();

		CommandHandlers.dtpCredit(player);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("TravelHomeCreated")));
	}

	/**
	 * Mounts the player and travels him to his home
	 */
	public static void goHome(Player player) {

		String name = player.getName().toLowerCase();
		if (!DragonTravelMain.players.hasIndex(name)) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("TravelHomeNotAvailable")));
			return;
		}

		if (!player.getWorld().toString().equalsIgnoreCase(DragonTravelMain.players.getString(name, "world"))) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("TravelHomeInDifferentWorld")));
			return;
		}

		double x = DragonTravelMain.players.getDouble(name, "x");
		double y = DragonTravelMain.players.getDouble(name, "y");
		double z = DragonTravelMain.players.getDouble(name, "z");

		Location loca = new Location(player.getWorld(), x, y, z);

		if (!(Travels.mountDragon(player)))
			return;

		XemDragon dragon = (XemDragon) DragonTravelMain.TravelInformation.get(player);
		dragon.startTravel(loca);
		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("TravelHomeTravelling")));
	}
}
