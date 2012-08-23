package com.xemsdoom.dt.modules;

import java.io.File;

import com.xemsdoom.dt.DragonTravelMain;

public class ConfigurationLoader {

	DragonTravelMain plugin;

	public ConfigurationLoader(DragonTravelMain plugin) {
		this.plugin = plugin;
	}

	public void loadConfig() {
		if (new File("plugins/DragonTravel/config.yml").exists()) {
			DragonTravelMain.config = plugin.getConfig();
			DragonTravelMain.config.options().copyDefaults(true);
			DragonTravelMain.log.info(String.format("[DragonTravel] Loaded Configuration"));
		} else {
			plugin.saveDefaultConfig();
			DragonTravelMain.config = plugin.getConfig();
			DragonTravelMain.config.options().copyDefaults(true);
			DragonTravelMain.log.info(String.format("[DragonTravel] Created Default Configuration"));
			DragonTravelMain.log.info(String.format("[DragonTravel] Loaded Configuration"));
		}
	}

	/**
	 * Checks if the configuration is the one of the current version of the
	 * plugin
	 * 
	 * @return true if the version matches, false when not
	 * 
	 */
	public boolean checkConfig() {

		if (!(DragonTravelMain.config.getDouble("ConfigVersion") == DragonTravelMain.ver)) {
			System.out.println("[DragonTravel] Configuration file is outdated!");
			System.out.println("[DragonTravel] Delete existing configuration file and generate a new one");
			return false;
		} else {
			DragonTravelMain.config = plugin.getConfig();
			DragonTravelMain.config.options().copyDefaults(true);
			return true;
		}
	}

}
