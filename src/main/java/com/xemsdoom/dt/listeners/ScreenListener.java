package com.xemsdoom.dt.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.modules.Stations;
import com.xemsdoom.dt.modules.Travels;
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
public class ScreenListener implements Listener {

	@EventHandler
	public void onButtonClick(ButtonClickEvent event) {

		SpoutPlayer player = event.getPlayer();

		if (!player.isSpoutCraftEnabled())
			return;

		if (MenuScreen.popcontainer == null || MenuScreen.popcontainer.isEmpty())
			return;

		if (!event.getButton().getScreen().getId().equals(MenuScreen.popcontainer.get(player)))
			return;

		if (event.getButton().getText().equals("Create Destination")) {

			MenuScreen sm = MenuScreen.screencontainer.get(player);

			if (sm.input1.getText().isEmpty()) {
				sm.input1.setText("Write something first");
				return;
			}

			Stations.setDestination(player, sm.input1.getText().trim());
			player.getMainScreen().closePopup();

		} else if (event.getButton().getText().equals("Create Station")) {

			MenuScreen sm = MenuScreen.screencontainer.get(player);

			if (sm.input3.getText().isEmpty()) {
				sm.input3.setText("Write something first");
				return;
			}

			Stations.setStation(player, sm.input3.getText().trim());
			player.getMainScreen().closePopup();

		} else if (event.getButton().getText().equals("Fly to Destination")) {

			MenuScreen sm = MenuScreen.screencontainer.get(player);

			if (sm.input2.getText().isEmpty()) {
				sm.input2.setText("Write something first");
				return;
			}

			String name = sm.input2.getText().trim();

			if (!DragonTravelMain.dbd.hasIndex(name)) {
				sm.input2.setText("Does not exist");
				return;
			}

			Travels.travelDestination(player, name);
			player.getMainScreen().closePopup();

		} else if (event.getButton().getText().equals("Fly to Player")) {

			MenuScreen sm = MenuScreen.screencontainer.get(player);

			if (sm.input4.getText().isEmpty()) {
				sm.input4.setText("Write something first");
				return;
			}

			String name = sm.input4.getText().trim();

			if (player.getServer().getPlayer(name) == null) {
				sm.input4.setText("Not online");
				return;
			}

			Travels.traveltoPlayer(player, name);
			player.getMainScreen().closePopup();
		}

	}
}
