package com.xemsdoom.dt.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.economy.EconomyHandler;
import com.xemsdoom.dt.modules.HomeTravel;
import com.xemsdoom.dt.modules.MessagesLoader;
import com.xemsdoom.dt.modules.StationaryDragon;
import com.xemsdoom.dt.modules.Stations;
import com.xemsdoom.dt.modules.Travels;
import com.xemsdoom.dt.movement.Flight;
import com.xemsdoom.dt.movement.FlightEditor;
import com.xemsdoom.dt.movement.FlightTravel;
import com.xemsdoom.dt.movement.Waypoint;
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
public class Commands implements CommandExecutor {

	DragonTravelMain plugin;

	// ChatColors
	public static final ChatColor red = ChatColor.RED;

	public Commands(DragonTravelMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

		// Length of the commands
		int length = args.length;

		// Return if not right length
		if (length < 1) {
			CommandHandlers.helpPlayer(sender);
			return false;
		}

		// Console Command Execution
		if (sender instanceof ConsoleCommandSender) {

			if (length > 0 && args[0].equalsIgnoreCase("remdragons")) {

				if (args.length == 1) {
					sender.sendMessage("[DragonTravel] /dt remdragons <worldname>");
					return false;
				}

				try {

					// Getting world in which we remove the dragons
					String worldStr = "";

					for (int i = 1; i < args.length; i++)
						worldStr += args[i] + " ";

					// Removing the dragons
					World world = plugin.getServer().getWorld(worldStr.trim());
					Travels.removeDragons(world);
					return true;
					
				} catch (Exception ex) {
					DragonTravelMain.log.info("[DragonTravel] Could not find the world specified. /dt remdragons worldname");
					return false;
				}

			} else {
				sender.sendMessage("[DragonTravel] Only /dt remdragons <worldname> is available from console");
				return false;
			}
		}

		// Getting the common argument 1
		String arg1 = args[0];

		// Getting player
		Player player = (Player) sender;

		// Player Command Execution
		switch (length) {

			case 1:

				if (arg1.equalsIgnoreCase("saveflight")) {

					if (!player.hasPermission("dt.saveflight")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!FlightEditor.editors.containsKey(player)) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("YouAreNotInEditMode")));
						return false;
					}

					if (FlightEditor.editors.get(player).wpcreatenum < 1) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("AtleastCreateOneWP")));
						return false;
					}

					FlightEditor.editors.get(player).saveFlight(player);
					FlightEditor.editors.remove(player);
					CommandHandlers.dtCredit(sender);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("SavedFlight")));
					return true;
				}

				if (arg1.equalsIgnoreCase("setwp")) {

					if (!player.hasPermission("dt.setwp")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!FlightEditor.editors.containsKey(player)) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("YouAreNotInEditMode")));
						return false;
					}

					Location loc = player.getLocation();
					Flight flight = FlightEditor.editors.get(player);
					Waypoint wp = new Waypoint();
					wp.setX(loc.getX());
					wp.setY(loc.getY());
					wp.setZ(loc.getZ());
					wp.setMarker(player);
					flight.addWaypoint(wp);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("SetWP")) + " " + flight.wpcreatenum);
					return true;
				}

				if (arg1.equalsIgnoreCase("remlastwp")) {

					if (!player.hasPermission("dt.remwp")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!FlightEditor.editors.containsKey(player)) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("YouAreNotInEditMode")));
						return false;
					}

					FlightEditor.editors.get(player).removeWaypoint();
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("RemovedLastWP")));
					return true;
				}

				if (arg1.equalsIgnoreCase("statdragon")) {

					if (!sender.hasPermission("dt.statdragon")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					StationaryDragon.createStatDragon(player);
					return true;
				}

				if (arg1.equalsIgnoreCase("sethome")) {

					if (!sender.hasPermission("dt.home.set")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					HomeTravel.setHome(player);
					return true;
				}

				if (arg1.equalsIgnoreCase("home")) {

					if (!sender.hasPermission("dt.home")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!EconomyHandler.chargePlayerHomeSet(player))
						return false;

					HomeTravel.goHome(player);
					return true;
				}

				if (arg1.equalsIgnoreCase("mount")) {

					if (!sender.hasPermission("dt.mount")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					Travels.mountDragon(player);
					return true;
				}

				if (arg1.equalsIgnoreCase("dismount")) {

					if (!sender.hasPermission("dt.dismount")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					Travels.dismountDragon(player);
					return true;
				}

				if (arg1.equalsIgnoreCase("remdragons")) {

					if (!sender.hasPermission("dt.remdragons")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					Travels.removeDragons(player);
					return true;
				}

				if (arg1.equalsIgnoreCase("destlist")) {

					if (!sender.hasPermission("dt.destlist")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!(DragonTravelMain.dbd.getIndices().isEmpty())) {
						Stations.showDestinations(player);
						return true;
					} else {
						CommandHandlers.dtCredit(sender);
						sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorNoDestinationsAvailable")));
						return false;
					}
				}

				if (arg1.equalsIgnoreCase("statlist")) {

					if (!sender.hasPermission("dt.statlist")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!(DragonTravelMain.dbs.getIndices().isEmpty())) {
						Stations.showStations(player);
						return true;
					} else {
						CommandHandlers.dtCredit(sender);
						sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorNoStationsAvailable")));
						return false;
					}
				}

				if (arg1.equalsIgnoreCase("ptoggle")) {

					if (!sender.hasPermission("dt.ptoggle")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (Travels.togglers.contains(player.getName())) {
						CommandHandlers.dtCredit(sender);
						sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("PlayerToggleOff")));
						Travels.togglers.remove(player.getName());
						return true;
					} else {
						Travels.togglers.add(player.getName());
						CommandHandlers.dtCredit(sender);
						sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("PlayerToggleOn")));
						return true;
					}
				}

				if (arg1.equalsIgnoreCase("stopmusic")) {

					if (!sender.hasPermission("dt.stopmusic")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					MusicHandler.stopEpicSound(player);
					return true;
				}

				if (arg1.equalsIgnoreCase("flightlist")) {
					if (!player.hasPermission("dt.flightlist")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					FlightTravel.showFlights(player);
					return true;
				}

				break;

			case 2:

				if (arg1.equalsIgnoreCase("flight")) {

					if (!player.hasPermission("dt.flight")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!Flight.existFlight(args[1])) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("FlightDoesNotExist")));
						return false;
					}

					Flight flight = new Flight(args[1]);
					FlightTravel.flyFlight(flight, player, true);
					return true;
				}

				if (arg1.equalsIgnoreCase("createflight")) {

					if (!player.hasPermission("dt.createflight")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (FlightEditor.editors.containsKey(player)) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("AlreadyInEditMode")));
						return false;
					}

					if (Flight.existFlight(args[1])) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("FlightAlreadyExists")));
						return false;
					}

					new FlightEditor(player, args[1]);
					CommandHandlers.dtCredit(sender);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("YouAreNowInEditMode")));
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpToMakeWP")));
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpToMakeWP2")));
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ExecuteSaveWhenDone")));
					return true;
				}

				if (arg1.equalsIgnoreCase("remflight")) {

					if (!player.hasPermission("dt.remflight")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!Flight.existFlight(args[1])) {
						CommandHandlers.dtCredit(sender);
						player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("FlightDoesNotExist")));
						return false;
					}

					Flight.removeFlight(args[1]);
					CommandHandlers.dtCredit(sender);
					player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("RemovedFlight")));
					return true;
				}

				if (arg1.equalsIgnoreCase("setdest")) {

					if (!sender.hasPermission("dt.setdest")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					String name = args[1];
					Stations.setDestination(player, name);
					return true;

				}

				if (arg1.equalsIgnoreCase("remdest")) {

					if (!sender.hasPermission("dt.remdest")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					String name = args[1];
					Stations.removeDestination(sender, name);
					return true;
				}

				if (arg1.equalsIgnoreCase("setstat")) {

					if (!sender.hasPermission("dt.setstat")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					String name = args[1];
					Stations.setStation(player, name);
					return true;
				}
				
				if(arg1.equalsIgnoreCase("remstat")){
					
					if (!sender.hasPermission("dt.remstat")) {
						CommandHandlers.noPerm(sender);
						return false;
					}
					
					Stations.removeStation(player, args[1]);
					return true;
				}

				if (arg1.equalsIgnoreCase("travel")) {

					if (!sender.hasPermission("dt.travel")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					if (!(DragonTravelMain.onlysigns)) {

						String name = args[1];
						Travels.travelDestination(player, name);
						return true;

					} else {
						CommandHandlers.dtCredit(sender);
						sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorCommandDisabled1")));
						sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorCommandDisabled2")));
						return false;
					}
				}

				if (arg1.equalsIgnoreCase("ptravel")) {

					if (!sender.hasPermission("dt.ptravel")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					String name = args[1];
					Travels.traveltoPlayer(player, name);
					return true;
				}
				
				if (arg1.equalsIgnoreCase("help")) {

					if (args[1].equalsIgnoreCase("1")) {
						CommandHandlers.commandList(sender);
						return true;
					} else if (args[1].equalsIgnoreCase("2")) {
						CommandHandlers.commandListTwo(sender);
						return true;
					} else {
						CommandHandlers.helpPlayer(sender);
						return false;
					}
				} 

				break;

			case 4:

				if (arg1.equalsIgnoreCase("ctravel")) {

					if (!sender.hasPermission("dt.ctravel")) {
						CommandHandlers.noPerm(sender);
						return false;
					}

					try {

						String xx = args[1];
						String yy = args[2];
						String zz = args[3];

						int x = Integer.parseInt(xx);
						int y = Integer.parseInt(yy);
						int z = Integer.parseInt(zz);

						Travels.travelChord(player, x, y, z);
						return true;
					} catch (NumberFormatException e) {
						CommandHandlers.dtCredit(sender);
						sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorNumbersAsCoords")));
						return false;
					}
				}

				break;

			default:
				CommandHandlers.helpPlayer(player);
				return false;
		}
		
		CommandHandlers.helpPlayer(player);
		return false;
	}
}
