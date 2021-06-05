package xyz.olivermartin.multichat.bungee;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * Configuration Handler Class
 * <p>Manages loading / creation of an individual configuration file</p>
 * 
 * @author Oliver Martin (Revilo410)
 *
 */
public class ConfigHandler {

	// The config file
	private Configuration config;
	// Path of config file
	private File configPath;
	// Name of config file
	private String fileName;

	public ConfigHandler(File configPath, String fileName) {

		this.configPath = configPath;
		this.config = null;
		this.fileName = fileName;
		this.startupConfig();

	}

	public Configuration getConfig() {
		if (config == null) startupConfig();
		return config;
	}

	public void startupConfig() {

		try {

			File file = new File(configPath, fileName);

			if (!file.exists()) {

				ProxyServer.getInstance().getLogger().info("Config file " + fileName + " not found... Creating new one.");
				saveDefaultConfig();

				loadConfig();

			} else {

				ProxyServer.getInstance().getLogger().info("Loading " + fileName + "...");
				loadConfig();

			}

		} catch (Exception e) {
			ProxyServer.getInstance().getLogger().info("[ERROR] Could not load  " + fileName);
			e.printStackTrace();
		}
	}

	private void saveDefaultConfig() {

		// Load default file into input stream
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

		// Copy to desired location
		try {
			Files.copy(inputStream, new File(configPath, fileName).toPath(), new CopyOption[0]);
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().info("[ERROR] Could not create new " + fileName + " file...");
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void loadConfig() {

		try {

			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(configPath, fileName));

		} catch (IOException e) {

			ProxyServer.getInstance().getLogger().info("[ERROR] Could not load " + fileName + " file...");
			e.printStackTrace();

		}
	}
}
