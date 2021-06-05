package xyz.olivermartin.multichat.local.spigot.listeners.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import xyz.olivermartin.multichat.local.common.listeners.chat.LocalChatListenerMonitor;
import xyz.olivermartin.multichat.local.common.listeners.chat.MultiChatLocalPlayerChatEvent;

public class LocalSpigotChatListenerMonitor extends LocalChatListenerMonitor implements Listener {

	@EventHandler(priority=EventPriority.MONITOR)
	public void onChat(final AsyncPlayerChatEvent event) {

		MultiChatLocalPlayerChatEvent mcce = new MultiChatLocalSpigotPlayerChatEvent(event);
		handleChat(mcce);

	}

}
