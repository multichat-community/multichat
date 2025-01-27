package xyz.olivermartin.multichat.local.spigot;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import xyz.olivermartin.multichat.local.common.LocalConsoleLogger;
import xyz.olivermartin.multichat.local.common.MultiChatLocalPlatform;

public class LocalSpigotConsoleLogger extends LocalConsoleLogger {

	private Logger logger;

	protected LocalSpigotConsoleLogger(Logger logger) {
		super(MultiChatLocalPlatform.SPIGOT);
	}

	@Override
	protected void displayMessageUsingLogger(String message) {
		logger.info(message);
	}

	@Override
	protected void sendColouredMessageToConsoleSender(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
