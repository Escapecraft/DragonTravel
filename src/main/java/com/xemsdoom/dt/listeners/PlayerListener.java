package com.xemsdoom.dt.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.XemDragon;
import com.xemsdoom.dt.commands.CommandHandlers;
import com.xemsdoom.dt.economy.EconomyHandler;
import com.xemsdoom.dt.modules.MessagesLoader;
import com.xemsdoom.dt.modules.Travels;
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
public class PlayerListener implements Listener {

	DragonTravelMain plugin;

	public PlayerListener(DragonTravelMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		if (DragonTravelMain.TravelInformation.containsKey(player)) {
			XemDragon dragon = DragonTravelMain.TravelInformation.get(player);
			Entity dragona = dragon.getBukkitEntity();
			dragona.remove();
			DragonTravelMain.TravelInformation.remove(player);

			// Find a safe Location so the player does not log on in the air
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
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDragonDismount(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (!DragonTravelMain.TravelInformation.containsKey(player))
			return;

		if (player.isInsideVehicle())
			return;

		try {

			MusicHandler.stopEpicSound(player);
			XemDragon dragon = DragonTravelMain.TravelInformation.get(player);
			Entity dra = dragon.getBukkitEntity();
			DragonTravelMain.TravelInformation.remove(player);
			DragonTravelMain.XemDragonRemoval.remove(dragon);
			dra.eject();
			dra.remove();

		} catch (Exception e) {
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDestinationSignInteract(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block block = event.getClickedBlock();

		if (block == null)
			return;

		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_AIR))
			return;

		for (String name : DragonTravelMain.signs.getIndices()) {

			double x = DragonTravelMain.signs.getDouble(name, "x");
			double y = DragonTravelMain.signs.getDouble(name, "y");
			double z = DragonTravelMain.signs.getDouble(name, "z");

			World world = block.getWorld();

			if (!world.toString().equalsIgnoreCase(DragonTravelMain.signs.getString(name, "world")))
				continue;

			Location compar = new Location(world, x, y, z);

			if (!compar.equals(block.getLocation()))
				continue;

			if (!(player.hasPermission("dt.travelsigns.use"))) {
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NoPermission")));
				return;
			}

			String dest = DragonTravelMain.signs.getString(name, "dest");

			if (!DragonTravelMain.dbd.hasIndex(dest) && !dest.equals(DragonTravelMain.config.getString("RandomDest-Name"))) {
				CommandHandlers.dtpCredit(player);
				player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorDestinationNotAvailable")));
				return;
			}

			if (!DragonTravelMain.signs.hasKey(name, "cost")) {
				Travels.mountDragon(player);
				Travels.travelDestinationSigns(player, dest);
				return;
			}

			double cost = DragonTravelMain.signs.getDouble(name, "cost");

			if (!player.hasPermission("dt.nocost"))
				if (!EconomyHandler.chargePlayerSigns(player, cost))
					return;

			Travels.mountDragon(player);
			Travels.travelDestinationSigns(player, dest);
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandPreventCMD(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		if (!DragonTravelMain.TravelInformation.containsKey(player))
			return;

		if (event.getMessage().equalsIgnoreCase("/dt home")) {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NotAllowedWhileMounted")));
			event.setCancelled(true);
			return;
		}

		String[] cmd = event.getMessage().split(" ");

		for (String message : DragonTravelMain.config.getStringList("CommandPrevent")) {

			if (!cmd[0].equalsIgnoreCase(message))
				continue;

			event.setCancelled(true);
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NotAllowedWhileMounted")));
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandPreventCHAT(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		if (!DragonTravelMain.TravelInformation.containsKey(player))
			return;

		for (String message : DragonTravelMain.config.getStringList("CommandPrevent")) {

			if (!event.getMessage().equalsIgnoreCase(message))
				continue;

			event.setCancelled(true);
			CommandHandlers.dtpCredit(player);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NotAllowedWhileMounted")));
			return;
		}
	}

	// TODO: FIX THIS - IT DOES NOT WORK!
	@EventHandler(priority = EventPriority.LOWEST)
	public void onStatDragonClick(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity clicked = event.getRightClicked();
		if (player.getItemInHand().getType() != Material.STICK)
			return;

		if (!player.hasPermission("dt.statdragon"))
			return;

		if (!(clicked instanceof XemDragon))
			return;

		if (clicked.getPassenger() != null)
			return;

		player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("RemovedStationaryDragon")));
		clicked.remove();
	}
}