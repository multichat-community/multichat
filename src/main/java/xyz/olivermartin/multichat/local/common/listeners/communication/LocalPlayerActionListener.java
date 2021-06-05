package xyz.olivermartin.multichat.local.common.listeners.communication;

import java.io.IOException;

import xyz.olivermartin.multichat.local.common.MultiChatLocal;
import xyz.olivermartin.multichat.local.common.listeners.LocalBungeeMessage;

public abstract class LocalPlayerActionListener {

	protected abstract void executeCommandForPlayersMatchingRegex(String playerRegex, String command);

	protected abstract void sendChatAsPlayer(String playerName, String rawMessage);

	protected boolean handleMessage(LocalBungeeMessage message) {

		try {

			String playerRegex = message.readUTF();
			String command = message.readUTF();

			// Handle the local global direct message hack
			if (isHackedMessage(command)) {
				handleHackedMessage(command, playerRegex);
				return true;
			}

			executeCommandForPlayersMatchingRegex(playerRegex, command);

			return true;

		} catch (IOException e) {

			MultiChatLocal.getInstance().getConsoleLogger().log("An error occurred trying to read local player action message from Bungeecord, is the server lagging?");
			return false;

		}

	}

	private boolean isHackedMessage(String command) {
		return (command.startsWith("!SINGLE L MESSAGE!") || command.startsWith("!SINGLE G MESSAGE!"));
	}

	private void handleHackedMessage(String command, String player) {

		String message = command.substring("!SINGLE X MESSAGE!".length(), command.length());

		if (command.startsWith("!SINGLE L MESSAGE!")) {
			MultiChatLocal.getInstance().getChatManager().queueChatChannel(player, "local");
		} else {
			MultiChatLocal.getInstance().getChatManager().queueChatChannel(player, "global");
		}

		sendChatAsPlayer(player, message);

	}

}
