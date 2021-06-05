package xyz.olivermartin.multichat.local.spigot;

import java.util.UUID;

import xyz.olivermartin.multichat.bungee.MultiChatUtil;
import xyz.olivermartin.multichat.local.common.LocalPlaceholderManager;
import xyz.olivermartin.multichat.local.common.MultiChatLocalPlatform;

public class LocalSpigotPlaceholderManager extends LocalPlaceholderManager {

	public LocalSpigotPlaceholderManager() {
		super(MultiChatLocalPlatform.SPIGOT);
	}

	@Override
	public String buildChatFormat(UUID uuid, String format) {

		// Reformat any hex codes in the format
		format = MultiChatUtil.reformatRGB(format);

		// RESPECT OTHER PLUGIN'S DISPLAY NAMES FIRST! (Allows for factions etc.)
		format = format.replace("%DISPLAYNAME%", "%1$s");

		// PROCESS REST ACCORDING TO MULTICHAT'S PLACEHOLDERS
		format = processMultiChatPlaceholders(uuid, format);

		// Adds the message on the end, respecting any changes from other plugins.
		return format + "%2$s"; // TODO This bit should not be added here, should be added in a different part (As sponge does not add here)

	}

}
