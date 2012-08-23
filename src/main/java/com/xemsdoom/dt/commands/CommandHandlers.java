package com.xemsdoom.dt.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xemsdoom.dt.DragonTravelMain;
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
public class CommandHandlers {

	private static ChatColor gold = ChatColor.GOLD;

	public static void noPerm(CommandSender sender) {
		sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NoPermission")));
	}

	public static void dtCredit(CommandSender sender) {
		sender.sendMessage(gold + ">DragonTravel<");
	}

	public static void dtpCredit(Player player) {
		player.sendMessage(gold + ">DragonTravel<");
	}

	public static void helpPlayer(CommandSender sender) {
		sender.sendMessage(gold + ">DragonTravel Commands Help<");
		sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("Help")));
	}

	public static void commandList(CommandSender sender) {

		sender.sendMessage(gold + ">DragonTravel Commands Page 1<");

		if (sender.hasPermission("dt.mount"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTmount")));
		if (sender.hasPermission("dt.dismount"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTdismount")));

		if (sender.hasPermission("dt.home.set"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTsethome")));
		if (sender.hasPermission("dt.home"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDThome")));

		if (sender.hasPermission("dt.flight"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpFlight")));
		if (sender.hasPermission("dt.flightlist"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpFlightList")));

		if (sender.hasPermission("dt.stopmusic"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTstopmusic")));
		if (sender.hasPermission("dt.travel"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTtravel")));
		if (sender.hasPermission("dt.destlist"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTdestlist")));
		if (sender.hasPermission("dt.statlist"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTstatlist")));

		if (sender.hasPermission("dt.ptoggle"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpTogglePTravel")));

		if (sender.hasPermission("dt.ctravel"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTctravel")));

		if (sender.hasPermission("dt.ptravel"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTptravel")));
	}

	public static void commandListTwo(CommandSender sender) {

		sender.sendMessage(gold + ">DragonTravel Commands Page 2<");

		if (sender.hasPermission("dt.setdest"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTsetdest")));
		if (sender.hasPermission("dt.setstat"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTsetstat")));
		if (sender.hasPermission("dt.remdest"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTremdest")));
		if (sender.hasPermission("dt.remstat"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTremstat")));
		if (sender.hasPermission("dt.createflight"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpCreateFlight")));
		if (sender.hasPermission("dt.remflight"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpRemFlight")));
		if (sender.hasPermission("dt.setwp"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpSetWP")));
		if (sender.hasPermission("dt.remwp"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpRemWP")));

		if (sender.hasPermission("dt.statdragon"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTstatdragon")));
		if (sender.hasPermission("dt.remdragons"))
			sender.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("HelpDTremdragons")));

	}
}
