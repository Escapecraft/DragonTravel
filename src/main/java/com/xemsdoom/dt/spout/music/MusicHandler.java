package com.xemsdoom.dt.spout.music;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

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
public class MusicHandler {

	static DragonTravelMain plugin;
	private static boolean sendplay = DragonTravelMain.sendplaymessage;

	public MusicHandler(DragonTravelMain main) {
		MusicHandler.plugin = main;
	}

	// Plays music for a player which is travelling
	public static void playEpicSound(Player player) {

		if (!DragonTravelMain.spout)
			return;

		SpoutPlayer spoutplayer = (SpoutPlayer) player;

		if (!spoutplayer.isSpoutCraftEnabled())
			return;

		try {
			DragonTravelMain.sound.playCustomMusic(plugin, spoutplayer, DragonTravelMain.config.getString("MusicURL"), sendplay);
		} catch (Exception e) {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("ErrorInvalidMusic")));
		}
	}

	// Stops the sound for a SpoutCraft player
	public static void stopEpicSound(Player player) {
		if (!DragonTravelMain.spout || player == null)
			return;
		SpoutPlayer spoutplayer = (SpoutPlayer) player;

		if (!spoutplayer.isSpoutCraftEnabled())
			return;

		DragonTravelMain.sound.stopMusic(spoutplayer);
	}
}
