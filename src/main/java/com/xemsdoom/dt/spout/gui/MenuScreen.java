package com.xemsdoom.dt.spout.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.xemsdoom.dt.DragonTravelMain;

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
public class MenuScreen extends DragonTravelMain {

	public GenericTextField input1;
	public GenericTextField input2;
	public GenericTextField input3;
	public GenericTextField input4;
	MenuScreen ms;

	public static HashMap<Player, UUID> popcontainer = new HashMap<Player, UUID>();
	public static HashMap<Player, MenuScreen> screencontainer = new HashMap<Player, MenuScreen>();
	public UUID screenId;
	@SuppressWarnings("unused")
	private Player player;

	public MenuScreen(Player player) {
		this.player = player;
		this.ms = this;
	}

	public void createGUI(Player player) {

		GenericPopup pop = new GenericPopup();
		this.screenId = pop.getId();

		popcontainer.put(player, screenId);
		screencontainer.put(player, ms);

		GenericTexture label1 = new GenericTexture();
		label1.setUrl("http://dl.dropbox.com/u/37237580/gui.jpg");
		label1.setHeight(46).setWidth(230);
		label1.setPriority(RenderPriority.High);
		label1.setAnchor(WidgetAnchor.TOP_CENTER);
		label1.shiftXPos(-115);

		GenericLabel label2 = new GenericLabel();
		label2.setText(ChatColor.GOLD + "Destination");
		label2.setAnchor(WidgetAnchor.TOP_LEFT);
		label2.shiftXPos(40).shiftYPos(50);

		GenericLabel label3 = new GenericLabel();
		label3.setText(ChatColor.GOLD + "Travel to Destination");
		label3.setAnchor(WidgetAnchor.TOP_CENTER);
		label3.shiftXPos(40).shiftYPos(50);

		GenericLabel label4 = new GenericLabel();
		label4.setText(ChatColor.GOLD + "Station");
		label4.setAnchor(WidgetAnchor.TOP_LEFT);
		label4.shiftXPos(40).shiftYPos(110);

		GenericLabel label5 = new GenericLabel();
		label5.setText(ChatColor.GOLD + "Travel to Player");
		label5.setAnchor(WidgetAnchor.TOP_CENTER);
		label5.shiftXPos(40).shiftYPos(110);

		input1 = new GenericTextField();
		input1.setMaximumLines(15);
		input1.setHeight(15).setWidth(200);
		input1.setAnchor(WidgetAnchor.TOP_LEFT);
		input1.shiftXPos(40).setY(60);
		input1.setMaximumLines(1);
		input1.setFocus(false);

		input2 = new GenericTextField();
		input2.setMaximumLines(15);
		input2.setHeight(15).setWidth(200);
		input2.setAnchor(WidgetAnchor.TOP_CENTER);
		input2.shiftXPos(40).setY(60);
		input2.setMaximumLines(1);
		input2.setFocus(false);

		input3 = new GenericTextField();
		input3.setMaximumLines(15);
		input3.setHeight(15).setWidth(200);
		input3.setAnchor(WidgetAnchor.TOP_LEFT);
		input3.shiftXPos(40).setY(120);
		input3.setMaximumLines(1);
		input3.setFocus(false);

		input4 = new GenericTextField();
		input4.setMaximumLines(15);
		input4.setHeight(15).setWidth(200);
		input4.setAnchor(WidgetAnchor.TOP_CENTER);
		input4.shiftXPos(40).setY(120);
		input4.setMaximumLines(1);
		input4.setFocus(false);

		GenericButton button1 = new GenericButton("Create Destination");
		button1.setAlign(WidgetAnchor.CENTER_CENTER);
		button1.setAnchor(WidgetAnchor.TOP_LEFT);
		button1.shiftXPos(40).shiftYPos(80);
		button1.setHeight(15).setWidth(200);
		button1.setHoverColor(new Color(1.0F, 5, 0, 1.0F));

		GenericButton button2 = new GenericButton("Fly to Destination");
		button2.setAlign(WidgetAnchor.CENTER_CENTER);
		button2.setAnchor(WidgetAnchor.TOP_CENTER);
		button2.shiftXPos(40).shiftYPos(80);
		button2.setHeight(15).setWidth(200);
		button2.setHoverColor(new Color(1.0F, 5, 0, 1.0F));

		GenericButton button3 = new GenericButton("Create Station");
		button3.setAlign(WidgetAnchor.CENTER_CENTER);
		button3.setAnchor(WidgetAnchor.TOP_LEFT);
		button3.shiftXPos(40).shiftYPos(140);
		button3.setHeight(15).setWidth(200);
		button3.setHoverColor(new Color(1.0F, 5, 0, 1.0F));

		GenericButton button4 = new GenericButton("Fly to Player");
		button4.setAlign(WidgetAnchor.CENTER_CENTER);
		button4.setAnchor(WidgetAnchor.TOP_CENTER);
		button4.shiftXPos(40).shiftYPos(140);
		button4.setHeight(15).setWidth(200);
		button4.setHoverColor(new Color(1.0F, 5, 0, 1.0F));

		pop.attachWidget(instance, label1);
		pop.attachWidget(instance, label2);
		pop.attachWidget(instance, label3);
		pop.attachWidget(instance, label4);
		pop.attachWidget(instance, label5);

		pop.attachWidget(instance, input1);
		pop.attachWidget(instance, input2);
		pop.attachWidget(instance, input3);
		pop.attachWidget(instance, input4);

		pop.attachWidget(instance, button1);
		pop.attachWidget(instance, button2);
		pop.attachWidget(instance, button3);
		pop.attachWidget(instance, button4);

		((SpoutPlayer) player).getMainScreen().attachPopupScreen(pop);

	}

}
