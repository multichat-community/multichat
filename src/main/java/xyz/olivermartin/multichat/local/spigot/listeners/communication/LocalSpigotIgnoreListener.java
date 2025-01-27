package xyz.olivermartin.multichat.local.spigot.listeners.communication;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import xyz.olivermartin.multichat.local.common.MultiChatLocal;
import xyz.olivermartin.multichat.local.common.listeners.LocalBungeeObjectMessage;
import xyz.olivermartin.multichat.local.common.listeners.communication.LocalIgnoreListener;
import xyz.olivermartin.multichat.local.spigot.listeners.SpigotBungeeObjectMessage;

public class LocalSpigotIgnoreListener extends LocalIgnoreListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		if (!channel.equals("multichat:ignore")) return;

		try {
			LocalBungeeObjectMessage lbm = new SpigotBungeeObjectMessage(message);

			handleMessage(lbm);

		} catch (IOException e) {
			MultiChatLocal.getInstance().getConsoleLogger().log("An error occurred reading the object stream in the local ignore listener...");
			return;
		}

	}

}
