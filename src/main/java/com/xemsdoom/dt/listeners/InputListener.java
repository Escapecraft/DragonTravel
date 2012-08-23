package com.xemsdoom.dt.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.modules.MessagesLoader;
import com.xemsdoom.dt.spout.gui.MenuScreen;

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
public class InputListener implements Listener {

	public DragonTravelMain plugin;

	@EventHandler
	public void onKeyPress(KeyPressedEvent event) {

		SpoutPlayer player = event.getPlayer();

		if (!player.isSpoutCraftEnabled())
			return;

		if (event.getScreenType() != ScreenType.GAME_SCREEN)
			return;

		String keypressed = event.getKey().name();

		if (!keypressed.equalsIgnoreCase(DragonTravelMain.config.getString("GUIopenKey")))
			return;

		if (!player.hasPermission("dt.gui")) {
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("NoPermission")));
			return;
		}

		MenuScreen pop = new MenuScreen(player);
		pop.createGUI(player);
	}

}
