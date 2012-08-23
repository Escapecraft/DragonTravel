package com.xemsdoom.dt.economy;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.commands.CommandHandlers;
import com.xemsdoom.dt.modules.MessagesLoader;

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
public class EconomyHandler {

	private static String withdrawmessage = DragonTravelMain.messages.getString("WithdrawMessage");
	private static String notenough = MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NotEnoughMoney"));
	Server server;

	public EconomyHandler(Server server) {
		this.server = server;
	}

	// Gets the economy plugin
	public boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			DragonTravelMain.Economy = economyProvider.getProvider();
			DragonTravelMain.EconomyEnabled = true;
		}

		return (DragonTravelMain.Economy != null);
	}

	// Charges the player for a normal travel
	public static boolean chargePlayer(Player player) {

		if (!DragonTravelMain.config.getBoolean("Economy"))
			return true;

		String name = player.getName();
		double balance = DragonTravelMain.Economy.getBalance(name);

		if (DragonTravelMain.config.getDouble("PayTravel") == 0.0)
			return true;

		if (balance < DragonTravelMain.config.getDouble("PayTravel")) {

			CommandHandlers.dtpCredit(player);
			player.sendMessage(notenough);
			return false;
		}

		DragonTravelMain.Economy.withdrawPlayer(name, DragonTravelMain.config.getDouble("PayTravel"));

		String msg = MessagesLoader.replaceColors(withdrawmessage);
		String msgm = msg.replace("%amount%", String.valueOf(DragonTravelMain.config.getDouble("PayTravel")));
		player.sendMessage(msgm);

		return true;
	}

	// Charges the player for a player to player travel
	public static boolean chargePlayerTravelPlayer(Player player) {

		if (!DragonTravelMain.config.getBoolean("Economy"))
			return true;

		String name = player.getName();
		double balance = DragonTravelMain.Economy.getBalance(name);

		if (DragonTravelMain.config.getDouble("PaytoPlayer") == 0.0)
			return true;

		if (balance < DragonTravelMain.config.getDouble("PaytoPlayer")) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(notenough);
			return false;
		}

		DragonTravelMain.Economy.withdrawPlayer(name, DragonTravelMain.config.getDouble("PaytoPlayer"));

		String msg = MessagesLoader.replaceColors(withdrawmessage);
		String msgm = msg.replace("%amount%", String.valueOf(DragonTravelMain.config.getDouble("PaytoPlayer")));
		player.sendMessage(msgm);

		return true;
	}

	// Charges the player for a chords travel
	public static boolean chargePlayerCoordsTravel(Player player) {

		if (!DragonTravelMain.config.getBoolean("Economy"))
			return true;

		String name = player.getName();
		double balance = DragonTravelMain.Economy.getBalance(name);

		if (DragonTravelMain.config.getDouble("PayCoords") != 0.0)
			return true;

		if (balance < DragonTravelMain.config.getDouble("PayCoords")) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(notenough);
			return false;
		}

		DragonTravelMain.Economy.withdrawPlayer(name, DragonTravelMain.config.getDouble("PayCoords"));

		String msg = MessagesLoader.replaceColors(withdrawmessage);
		String msgm = msg.replace("%amount%", String.valueOf(DragonTravelMain.config.getDouble("PayCoords")));
		player.sendMessage(msgm);

		return true;
	}

	// Charges the players for a sign travel
	public static boolean chargePlayerSigns(Player player, Double cost) {

		if (player.hasPermission("dt.nocost"))
			return true;

		if (!DragonTravelMain.config.getBoolean("Economy"))
			return true;

		String name = player.getName();
		double balance = DragonTravelMain.Economy.getBalance(name);

		if (cost == 0)
			return true;

		if (balance < cost) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(notenough);
			return false;
		}

		DragonTravelMain.Economy.withdrawPlayer(name, cost);

		String msg = MessagesLoader.replaceColors(withdrawmessage);
		String msgm = msg.replace("%amount%", String.valueOf(cost));
		player.sendMessage(msgm);

		return true;
	}

	// Charges the player for a home travel
	public static boolean chargePlayerHome(Player player) {

		if (!DragonTravelMain.config.getBoolean("Economy"))
			return true;

		String name = player.getName();
		double balance = DragonTravelMain.Economy.getBalance(name);

		if (DragonTravelMain.config.getDouble("PayHome") == 0.0)
			return true;

		if (balance < DragonTravelMain.config.getDouble("PayHome")) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(notenough);
			return false;
		}

		DragonTravelMain.Economy.withdrawPlayer(name, DragonTravelMain.config.getDouble("PayHome"));

		String msg = MessagesLoader.replaceColors(withdrawmessage);
		String msgm = msg.replace("%amount%", String.valueOf(DragonTravelMain.config.getDouble("PayHome")));
		player.sendMessage(msgm);
		return true;
	}

	// Charges the player for a home set
	public static boolean chargePlayerHomeSet(Player player) {
		if (!DragonTravelMain.config.getBoolean("Economy"))
			return true;

		String name = player.getName();
		double balance = DragonTravelMain.Economy.getBalance(name);

		if (DragonTravelMain.config.getDouble("HomeSetCost") == 0.0)
			return true;

		if (balance < DragonTravelMain.config.getDouble("HomeSetCost")) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(notenough);
			return false;
		}

		DragonTravelMain.Economy.withdrawPlayer(name, DragonTravelMain.config.getDouble("HomeSetCost"));

		String msg = MessagesLoader.replaceColors(withdrawmessage);
		String msgm = msg.replace("%amount%", String.valueOf(DragonTravelMain.config.getDouble("HomeSetCost")));
		player.sendMessage(msgm);
		return true;
	}

	// Charges the player for a home set
	public static boolean chargeFlightTravel(Player player) {
		if (!DragonTravelMain.config.getBoolean("Economy"))
			return true;

		String name = player.getName();
		double balance = DragonTravelMain.Economy.getBalance(name);

		if (DragonTravelMain.config.getDouble("PayFlight") == 0.0)
			return true;

		if (balance < DragonTravelMain.config.getDouble("PayFlight")) {
			CommandHandlers.dtpCredit(player);
			player.sendMessage(notenough);
			return false;
		}

		DragonTravelMain.Economy.withdrawPlayer(name, DragonTravelMain.config.getDouble("PayFlight"));

		String msg = MessagesLoader.replaceColors(withdrawmessage);
		String msgm = msg.replace("%amount%", String.valueOf(DragonTravelMain.config.getDouble("PayFlight")));
		player.sendMessage(msgm);
		return true;
	}
}
