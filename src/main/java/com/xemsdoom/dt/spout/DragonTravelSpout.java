package com.xemsdoom.dt.spout;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;

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
public class DragonTravelSpout {

	// Gets the instance of Spout
	public static boolean getSpout() {
		
		Plugin plugin = DragonTravelMain.pm.getPlugin("Spout");
		
		if (plugin == null)
			return false;
		
		DragonTravelMain.spout = true;
		DragonTravelMain.sound = SpoutManager.getSoundManager();
		return true;
	}

}
