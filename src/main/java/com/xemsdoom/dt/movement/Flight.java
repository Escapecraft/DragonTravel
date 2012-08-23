package com.xemsdoom.dt.movement;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import com.xemsdoom.mexdb.exception.EmptyIndexException;
import com.xemsdoom.mexdb.system.Entry;

import static com.xemsdoom.dt.DragonTravelMain.*;

public class Flight {

	HashMap<Integer, Waypoint> waypoints = new HashMap<Integer, Waypoint>();
	int currentwp = 0;
	public int wpcreatenum = 0;
	String name;

	/**
	 * Flight object, containing a flight-name and waypoints
	 * 
	 * @param name
	 */
	public Flight(String name) {
		this.name = name;
	}

	/**
	 * Checks if the flight entry exists in the db
	 * 
	 * @param name
	 * @return
	 */
	public static boolean existFlight(String name) {
		if (wps.hasIndex(name))
			return true;
		else
			return false;
	}

	/**
	 * Removes the specific flight out of the db
	 * 
	 * @param name
	 */
	public static void removeFlight(String name) {
		wps.removeEntry(name);
		wps.push();
	}

	/**
	 * Saves the not yet saved flight to the db
	 */
	public void saveFlight(Player player) {

		Entry entry = null;

		try {
			entry = new Entry(name);
		} catch (EmptyIndexException e) {
			e.printStackTrace();
		}

		int wpamount = waypoints.size();

		for (int counter = 0; counter < wpamount; counter++) {
			Waypoint wp = waypoints.get(counter);

			Chunk chunk = player.getWorld().getBlockAt((int) wp.getX(), (int) wp.getY(), (int) wp.getZ()).getChunk();
			
			chunk.load(true);
			
			String x = String.valueOf(wp.getX());
			String y = String.valueOf(wp.getY());
			String z = String.valueOf(wp.getZ());
			
			wp.removeMarker();
			entry.addValue(String.valueOf(counter), x + "," + y + "," + z);
		}

		wps.addEntry(entry);
		wps.push();
	}

	/**
	 * Adds a waypoint to the db as a key/keyvalue
	 * 
	 * @param wp
	 */
	public void addWaypoint(Waypoint wp) {
		waypoints.put(wpcreatenum, wp);
		wpcreatenum++;
	}

	/**
	 * Removes the last waypoint
	 * 
	 * @param wp
	 */
	public void removeWaypoint() {
		if (wpcreatenum == 0)
			return;
		wpcreatenum--;
		waypoints.get(wpcreatenum).removeMarker();
		waypoints.remove(wpcreatenum);
	}

	/**
	 * Gets the firstwaypoint
	 * 
	 * @return
	 */
	public Waypoint getFirstWaypoint() {
		Waypoint wp = waypoints.get(currentwp);
		currentwp++;
		return wp;
	}

	/**
	 * Gets the next waypoint for this flight
	 */
	public Waypoint getNextWaypoint() {
		Waypoint wp = waypoints.get(currentwp);
		currentwp++;
		return wp;
	}

	/**
	 * Loads the waypoints for this flight into the waypoints-hashmap
	 * 
	 * @param destname
	 */
	public void loadWPs() {

		Pattern pattern = Pattern.compile(",");
		int counter = 0;

		for (String value : wps.getValues(name)) {

			// Creating new Waypoint
			Waypoint wp = new Waypoint();

			// Adding coords to it
			String[] coords = pattern.split(value);
			wp.setX(Double.valueOf(coords[0]));
			wp.setY(Double.valueOf(coords[1]));
			wp.setZ(Double.valueOf(coords[2]));

			// Puting in HashMap
			waypoints.put(counter, wp);
			counter++;
		}
	}

}
