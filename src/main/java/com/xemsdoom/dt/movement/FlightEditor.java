package com.xemsdoom.dt.movement;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.dt.modules.MessagesLoader;

public class FlightEditor implements Listener {

	public static HashMap<Player, Flight> editors = new HashMap<Player, Flight>();

	public FlightEditor() {
	}

	public FlightEditor(Player player, String name) {
		editors.put(player, new Flight(name));
	}

	/**
	 * Checks if the passed player is an editor
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isEditor(Player player) {
		if (editors.containsKey(player))
			return true;
		else
			return false;
	}

	/**
	 * Removes the passed player from editor mode
	 * 
	 * @param player
	 */
	public static void removeEditor(Player player) {
		editors.remove(player);
	}

	@EventHandler
	public void onWP(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Location loc = player.getLocation();

		if (!editors.containsKey(player))
			return;

		if (player.getItemInHand().getTypeId() != 281)
			return;

		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			editors.get(player).removeWaypoint();
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("RemovedLastWP")));
			return;
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

			Flight flight = editors.get(player);
			Waypoint wp = new Waypoint();
			wp.setX(loc.getX());
			wp.setY(loc.getY());
			wp.setZ(loc.getZ());
			wp.setMarker(player);
			flight.addWaypoint(wp);
			player.sendMessage(MessagesLoader.replaceColors(DragonTravelMain.messages.getString("SetWP")) + " " + flight.wpcreatenum);
		}
	}

}
