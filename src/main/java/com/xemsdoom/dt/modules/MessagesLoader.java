package com.xemsdoom.dt.modules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.xemsdoom.dt.DragonTravelMain;

public class MessagesLoader extends JavaPlugin {

	/**
	 * Checks if the messages file has the right version
	 */
	public boolean checkMessagesFileVersion(double version) {
		if (version != DragonTravelMain.messagesVer) {
			System.err.println("[DragonTravel] Messages file is outdated!");
			System.err.println("[DragonTravel] Delete existing Messages-File and generate a new one");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Initializes the messages file in the main class
	 */
	public void initMessages() {

		DragonTravelMain.messagesFile = new File("plugins/DragonTravel", "messages.yml");
		
		try {
			firstRun();
		} catch (Exception e) {
			System.out.println("[DragonTravel] Error while initializing Messages-File");
			e.printStackTrace();
		}

		DragonTravelMain.messages = new YamlConfiguration();
		loadMessages();
	}

	/**
	 * Checks if the resource messages.yml already got copied<br>
	 * and otherwhise copyies it to the plugin folder of DragonTravel
	 * 
	 * @throws Exception
	 */
	private void firstRun() throws Exception {
		if (DragonTravelMain.messagesFile.exists())
			return;

		DragonTravelMain.messagesFile.createNewFile();

		copy(getClass().getResourceAsStream("messages.yml"), DragonTravelMain.messagesFile);

		System.out.println("[DragonTravel] Created Messages-File");
	}

	/**
	 * Copy the resource messages.yml to the local messages.yml
	 * 
	 * @param in
	 * @param file
	 */
	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the content of the messages.yml into to the memory as "messages"
	 */
	public void loadMessages() {
		try {

			DragonTravelMain.messages.load(DragonTravelMain.messagesFile);

			if (!(checkMessagesFileVersion(DragonTravelMain.messages.getDouble("MessagesVersion")))) {
				DragonTravelMain.instance.getPluginLoader().disablePlugin(DragonTravelMain.instance);
				return;
			}

			System.out.println("[DragonTravel] Loaded Messages-File");

		} catch (Exception e) {
			System.out.println("[DragonTravel] No Messages-File found");
			e.printStackTrace();
		}
	}

	/**
	 * Saves the messages memoryprint to the disk
	 */
	public void saveMessages() {
		try {

			DragonTravelMain.messages.save(DragonTravelMain.messagesFile);

		} catch (IOException e) {
			System.out.println("[DragonTravel] Could not save Messages-File");
			e.printStackTrace();
		}
	}

	public static String replaceColors(String string) {
		return string.replaceAll("(?i)&([a-f0-9])", "\u00A7$1");
	}

}
