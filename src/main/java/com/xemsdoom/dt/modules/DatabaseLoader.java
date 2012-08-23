package com.xemsdoom.dt.modules;

import java.io.File;

import com.xemsdoom.dt.DragonTravelMain;
import com.xemsdoom.mexdb.MexDB;

public class DatabaseLoader extends DragonTravelMain {

	/**
	 * Creates new datbases if they do not exist
	 */
	public static void loadDatabase() {
		if (!(new File("plugins/DragonTravel/databases/Destinations.mini").exists())) {
			dbd = new MexDB("plugins/DragonTravel/databases", "Destinations");
			log.info(String.format("[DragonTravel] Created Destinations Database"));
		} else {
			dbd = new MexDB("plugins/DragonTravel/databases", "Destinations");
			log.info(String.format("[DragonTravel] Loaded Destinations"));
		}

		if (!(new File("plugins/DragonTravel/databases/Flights.mini").exists())) {
			wps = new MexDB("plugins/DragonTravel/databases", "Flights");
			log.info(String.format("[DragonTravel] Created Flights Database"));
		} else {
			wps = new MexDB("plugins/DragonTravel/databases", "Flights");
			log.info(String.format("[DragonTravel] Loaded Flights"));
		}

		if (!(new File("plugins/DragonTravel/databases/Stations.mini").exists())) {
			dbs = new MexDB("plugins/DragonTravel/databases", "Stations");
			log.info(String.format("[DragonTravel] Created Stations Database"));
		} else {
			dbs = new MexDB("plugins/DragonTravel/databases", "Stations.mini");
			log.info(String.format("[DragonTravel] Loaded Stations"));
		}

		if (!(new File("plugins/DragonTravel/databases/Signs.mini").exists())) {
			signs = new MexDB("plugins/DragonTravel/databases", "Signs");
			log.info(String.format("[DragonTravel] Created Signs Database"));
		} else {
			signs = new MexDB("plugins/DragonTravel/databases", "Signs");
			log.info(String.format("[DragonTravel] Loaded Signs"));
		}

		if (!(new File("plugins/DragonTravel/databases/Players.mini").exists())) {
			players = new MexDB("plugins/DragonTravel/databases", "Players");
			log.info(String.format("[DragonTravel] Created Homes Database"));
		} else {
			players = new MexDB("plugins/DragonTravel/databases", "Players");
			log.info(String.format("[DragonTravel] Loaded Homes"));
		}
	}
}
