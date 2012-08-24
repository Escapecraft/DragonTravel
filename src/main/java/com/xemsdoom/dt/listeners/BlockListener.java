package com.xemsdoom.dt.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.commands.CommandHandlers;
import com.xemsdoom.dt.modules.MessagesLoader;
import com.xemsdoom.dt.modules.SignCreating;
import com.xemsdoom.dt.movement.Flight;
import com.xemsdoom.dt.movement.Waypoint;

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
public class BlockListener implements Listener {

	DragonTravelMain plugin;

	public BlockListener(DragonTravelMain plugin) {
		this.plugin = plugin;
	}

	// Checking what is written on a sign and then fire the specific method
	@EventHandler(priority = EventPriority.LOW)
	public void onSignChange(SignChangeEvent event) {
		Block b = event.getBlock();
		Player player = event.getPlayer();

		if (!event.getLine(0).equalsIgnoreCase("[DragonTravel]"))
			return;

		// FLIGHTSIGNS
		if (player.hasPermission("dt.flightsigns")) {
			// Creating flight sign
			if (event.getLine(1).equalsIgnoreCase("Flight") && !event.getLine(2).isEmpty() && event.getLine(3).isEmpty()) {

				if (DragonTravelMain.EconomyEnabled) {
					CommandHandlers.dtCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorFourthLineMissing")));
					event.setCancelled(true);
					return;
				}

				if (!Flight.existFlight(event.getLine(2))) {
					CommandHandlers.dtCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("FlightDoesNotExist")));
					return;
				}

				CommandHandlers.dtCredit(player);
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("FlightSignCreated")));
				SignCreating.createFlightSign(event.getLine(2), event);
				return;
			}

			// Creatig flight economy sign
			if (event.getLine(1).equalsIgnoreCase("Flight") && !event.getLine(2).isEmpty() && !event.getLine(3).isEmpty()) {

				if (!DragonTravelMain.EconomyEnabled) {
					CommandHandlers.dtCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorEconomyNotActivated1")));
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorEconomyNotActivated2")));
					event.setCancelled(true);
					return;
				}

				if (!Flight.existFlight(event.getLine(2))) {
					CommandHandlers.dtCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("FlightDoesNotExist")));
					return;
				}

				int cost = 0;

				try {
					cost = Integer.parseInt(event.getLine(3));
				} catch (NumberFormatException e) {
					CommandHandlers.dtpCredit(player);
					CommandHandlers.dtCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorFourthLineInvalid")));
					event.setCancelled(true);
					return;
				}

				CommandHandlers.dtCredit(player);
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("FlightSignCreated")));
				SignCreating.createFlightSignEconomy(event.getLine(2), event, cost);
				return;
			}
		}

		// TRAVELSIGNS
		if (player.hasPermission("dt.travelsigns")) {

			// Naming destination signs
			if (event.getLine(1).isEmpty() && event.getLine(2).isEmpty() && event.getLine(3).isEmpty()) {
				SignCreating.nameDestinationSign(b, event, player);
				return;
			}

			// Naming station signs
			if (event.getLine(1).equalsIgnoreCase("name") && event.getLine(2).isEmpty() && event.getLine(3).isEmpty()) {
				SignCreating.nameStationSign(b, event, player);
				return;
			}

			String destination = event.getLine(2).toString();

			// Checking if even the destination exists
			if (!DragonTravelMain.dbd.hasIndex(destination) && !destination.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
				CommandHandlers.dtpCredit(player);
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorDestinationNotAvailable")));
				event.setCancelled(true);
				return;
			}

			// Checking if we have to make a sign with a cost or not and then
			// fire the specific method
			if (!DragonTravelMain.EconomyEnabled) {

				if (!event.getLine(3).isEmpty()) {
					CommandHandlers.dtpCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorEconomyNotActivated1")));
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorEconomyNotActivated2")));
					event.setCancelled(true);
					return;
				}

				// Creating a destination sign
				if (!event.getLine(2).isEmpty() && event.getLine(1).isEmpty()) {
					SignCreating.createDestinationSign(b, event, player, destination);
					return;
				}

			} else {

				// If not written a cost on the fourth line while having economy
				// enabled
				if (event.getLine(3).isEmpty()) {
					CommandHandlers.dtpCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorFourthLineMissing")));
					event.setCancelled(true);
					return;
				}

				int cost = 0;

				// Checking if the cost enteres is really a number
				try {
					cost = Integer.parseInt(event.getLine(3));
				} catch (NumberFormatException e) {
					CommandHandlers.dtpCredit(player);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorFourthLineInvalid")));
					event.setCancelled(true);
					return;
				}

				// Creating an economy sign
				if (event.getLine(1).isEmpty() && !event.getLine(2).isEmpty()) {
					SignCreating.createDestinationSignEconomy(b, event, player, cost, destination);
					return;
				}
			}
		}
	}

	@EventHandler
	public void onMarkerDestroy(BlockBreakEvent event) {
		if (Waypoint.markers.containsKey(event.getBlock())) {
			event.getPlayer().sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("MarkersBreak")));
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDestinationSignDestory(BlockBreakEvent event) {

		Block block = event.getBlock();
		Player player = event.getPlayer();
		String worldname = block.getLocation().getWorld().toString();

		if (block == null || block.getTypeId() == 0)
			return;

		if (block.getType() != Material.SIGN && block.getType() != Material.SIGN_POST)
			return;

		for (String name : DragonTravelMain.signs.getIndices()) {

			double x = DragonTravelMain.signs.getDouble(name, "x");
			double y = DragonTravelMain.signs.getDouble(name, "y");
			double z = DragonTravelMain.signs.getDouble(name, "z");

			if (!worldname.equalsIgnoreCase(DragonTravelMain.signs.getString(name, "world")))
				continue;

			World world = block.getWorld();
			Location compar = new Location(world, x, y, z);

			if (!compar.equals(block.getLocation()))
				continue;

			if (!(player.hasPermission("dt.travelsigns"))) {
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NoPermission")));
				event.setCancelled(true);
				break;
			}

			CommandHandlers.dtpCredit(player);
			DragonTravelMain.signs.removeEntry(name);
			DragonTravelMain.signs.push();
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("SignRemovedSuccessfully")));
		}
	}
}
