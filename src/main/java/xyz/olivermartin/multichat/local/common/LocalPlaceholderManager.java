package xyz.olivermartin.multichat.local.common;

import java.util.UUID;

public abstract class LocalPlaceholderManager {

	private MultiChatLocalPlatform platform;

	public LocalPlaceholderManager(MultiChatLocalPlatform platform) {
		this.platform = platform;
	}

	public MultiChatLocalPlatform getPlatform() {
		return this.platform;
	}

	/**
	 * This method builds the format for a chat message on the respective platform
	 * 
	 * <p>This should respect other plugins where possible, for example replacing displayname with %1$s on spigot.</p>
	 * 
	 * @param uuid
	 * @param format
	 * @return The built chat format
	 */
	public abstract String buildChatFormat(UUID uuid, String format);

	/**
	 * This method replaces all placeholders according to MultiChat's rules (doesn't pay attention to other plugins)
	 * 
	 * @param uuid
	 * @param message
	 * @return The message with all MultiChat placeholders replaced
	 */
	public String processMultiChatPlaceholders(UUID uuid, String message) {

		LocalConsoleLogger logger = MultiChatLocal.getInstance().getConsoleLogger();

		logger.debug("---------------------------");

		logger.debug("Processing placeholders...");

		logger.debug("INPUT FORMAT = " + message);
		logger.debug("INPUT FORMAT (visualised) = " + message.replace("&", "(#d)").replace("�", "(#e)"));

		//logger.debug("%NAME% = " + MultiChatLocal.getInstance().getNameManager().getName(uuid));
		if (message.contains("%NAME%")) message = message.replace("%NAME%", MultiChatLocal.getInstance().getNameManager().getName(uuid));
		//logger.debug("%NICK% = " + MultiChatLocal.getInstance().getMetaManager().getNick(uuid));
		if (message.contains("%NICK%")) message = message.replace("%NICK%", MultiChatLocal.getInstance().getMetaManager().getNick(uuid));
		//logger.debug("%DISPLAYNAME% = " + MultiChatLocal.getInstance().getMetaManager().getDisplayName(uuid));
		if (message.contains("%DISPLAYNAME%")) message = message.replace("%DISPLAYNAME%", MultiChatLocal.getInstance().getMetaManager().getDisplayName(uuid));
		//logger.debug("%PREFIX% = " + MultiChatLocal.getInstance().getMetaManager().getPrefix(uuid));
		if (message.contains("%PREFIX%")) message = message.replace("%PREFIX%", MultiChatLocal.getInstance().getMetaManager().getPrefix(uuid));
		//logger.debug("%SUFFIX% = " + MultiChatLocal.getInstance().getMetaManager().getSuffix(uuid));
		if (message.contains("%SUFFIX%")) message = message.replace("%SUFFIX%", MultiChatLocal.getInstance().getMetaManager().getSuffix(uuid));
		//logger.debug("%WORLD% = " + MultiChatLocal.getInstance().getMetaManager().getWorld(uuid));
		if (message.contains("%WORLD%")) message = message.replace("%WORLD%", MultiChatLocal.getInstance().getMetaManager().getWorld(uuid));
		//logger.debug("%SERVER% = " + MultiChatLocal.getInstance().getConfigManager().getLocalConfig().getServerName());
		if (message.contains("%SERVER%")) message = message.replace("%SERVER%", MultiChatLocal.getInstance().getConfigManager().getLocalConfig().getServerName());

		logger.debug("Final Message = " + message);
		logger.debug("Final Message (visualised) = " + message.replace("&", "(#d)").replace("�", "(#e)"));

		logger.debug("---------------------------");

		return message;

	}

}
