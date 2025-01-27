package xyz.olivermartin.multichat.bungee;

import java.util.UUID;

import com.olivermartin410.plugins.TGroupChatInfo;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.olivermartin.multichat.bungee.commands.GCCommand;

/**
 * Group Chat Management Class
 * <p>Handles Group Chat Operations</p>
 * 
 * @author Oliver Martin (Revilo410)
 * 
 */
public class GroupManager {

	/**
	 * Creates a new informal group chat based on the specified parameters
	 * Also adds the creator to the group as the owner
	 */
	public void createGroup(String groupname, UUID owneruuid, boolean secret, String password) {

		TGroupChatInfo newgroup = new TGroupChatInfo();

		newgroup.addMember(owneruuid);
		newgroup.addViewer(owneruuid);
		newgroup.addAdmin(owneruuid);
		newgroup.setName(groupname.toLowerCase());
		newgroup.setChatColor(ConfigManager.getInstance().getHandler("config.yml").getConfig().getString("groupchat.ccdefault").toCharArray()[0]);
		newgroup.setNameColor(ConfigManager.getInstance().getHandler("config.yml").getConfig().getString("groupchat.ncdefault").toCharArray()[0]);
		newgroup.setSecret(secret);
		newgroup.setPassword(password);
		newgroup.setFormal(false);

		MultiChat.groupchats.put(groupname.toLowerCase(), newgroup);

	}

	/**
	 * Adds a player to a group chat while removing them from the spy list if they were spying on it before
	 * This will also check if they are banned and stop them being added
	 * It will also check if they are already a member
	 * Passwords for the group are also checked
	 */
	public boolean joinGroup(String groupname, ProxiedPlayer player, String password) {

		boolean success = false;

		TGroupChatInfo groupchat = new TGroupChatInfo();
		groupchat = (TGroupChatInfo)MultiChat.groupchats.get(groupname.toLowerCase());

		if (!groupchat.existsBanned(player.getUniqueId())) {

			if (!groupchat.existsMember(player.getUniqueId())) {

				if (!groupchat.getSecret()) {

					if (groupchat.existsViewer(player.getUniqueId())) {

						if (player.hasPermission("multichat.staff.spy")) {

							MessageManager.sendSpecialMessage(player, "command_group_spy_off", groupname.toUpperCase());
							groupchat.delViewer(player.getUniqueId());

						} else {

							groupchat.delViewer(player.getUniqueId());

						}

					}

					groupchat.addMember(player.getUniqueId());
					groupchat.addViewer(player.getUniqueId());

					MultiChat.groupchats.remove(groupname.toLowerCase());
					MultiChat.groupchats.put(groupname.toLowerCase(), groupchat);

					success = true;

				} else {

					if (password.equals("")) {

						MessageManager.sendSpecialMessage(player, "groups_password_protected", groupname.toUpperCase());

					} else {

						if (password.equals(groupchat.getPassword())) {

							if (groupchat.existsViewer(player.getUniqueId())) {

								if (player.hasPermission("multichat.staff.spy")) {

									MessageManager.sendSpecialMessage(player, "command_group_spy_off", groupname.toUpperCase());
									groupchat.delViewer(player.getUniqueId());

								} else {
									groupchat.delViewer(player.getUniqueId());
								}

							}

							groupchat.addMember(player.getUniqueId());
							groupchat.addViewer(player.getUniqueId());

							MultiChat.groupchats.remove(groupname.toLowerCase());
							MultiChat.groupchats.put(groupname.toLowerCase(), groupchat);

							success = true;

						} else {

							MessageManager.sendSpecialMessage(player, "groups_password_incorrect", groupname.toUpperCase());

						}

					}
				}

			} else {
				MessageManager.sendSpecialMessage(player, "groups_already_joined", groupname.toUpperCase());
			}

		} else {
			MessageManager.sendSpecialMessage(player, "groups_banned", groupname.toUpperCase());
		}

		groupchat = null;
		return success;

	}

	/**
	 * Sets the selected group of a player to the specified group
	 */
	public void setViewedChat(UUID playeruuid, String groupname) {

		String viewedchat = (String)MultiChat.viewedchats.get(playeruuid);

		viewedchat = groupname.toLowerCase();
		MultiChat.viewedchats.remove(playeruuid);
		MultiChat.viewedchats.put(playeruuid, viewedchat);

	}

	/**
	 * The INFO announce in a group that a player has joined
	 */
	public void announceJoinGroup(String playername, String groupname) {

		GCCommand.sendMessage(playername + MessageManager.getMessage("groups_info_joined"), "&lINFO", MultiChat.groupchats.get(groupname.toLowerCase()));

	}

	/**
	 * The INFO announce in a group that a player has left
	 */
	public void announceQuitGroup(String playername, String groupname) {

		GCCommand.sendMessage(playername + MessageManager.getMessage("groups_info_quit"), "&lINFO", MultiChat.groupchats.get(groupname.toLowerCase()));

	}

	/**
	 * Quits a group, announces in the group chat and notifies the player quitting
	 */
	public void quitGroup(String groupname, UUID player, ProxiedPlayer pinstance) {

		TGroupChatInfo groupchatinfo = new TGroupChatInfo();
		String viewedchat = (String)MultiChat.viewedchats.get(player);

		groupchatinfo = (TGroupChatInfo)MultiChat.groupchats.get(groupname.toLowerCase());

		if (groupchatinfo.existsMember(player)) {

			if ((!groupchatinfo.existsAdmin(player)) || (groupchatinfo.getAdmins().size() > 1)) {

				groupchatinfo.delMember(player);
				groupchatinfo.delViewer(player);

				if (groupchatinfo.existsAdmin(player)) {
					groupchatinfo.delAdmin(player);
				}

				viewedchat = null;

				MultiChat.viewedchats.remove(player);
				MultiChat.viewedchats.put(player, viewedchat);
				MultiChat.groupchats.remove(groupname.toLowerCase());
				MultiChat.groupchats.put(groupname.toLowerCase(), groupchatinfo);

				MessageManager.sendSpecialMessage(pinstance, "groups_quit", groupname.toUpperCase());
				announceQuitGroup(pinstance.getName(), groupname);

			} else if (!groupchatinfo.getFormal()) {

				MessageManager.sendSpecialMessage(pinstance, "groups_cannot_quit_owner_1", groupname.toUpperCase());
				MessageManager.sendSpecialMessage(pinstance, "groups_cannot_quit_owner_2", groupname.toUpperCase());

			} else {

				MessageManager.sendSpecialMessage(pinstance, "groups_cannot_quit_admin_1", groupname.toUpperCase());
				MessageManager.sendSpecialMessage(pinstance, "groups_cannot_quit_admin_2", groupname.toUpperCase());
			}

		} else {

			MessageManager.sendSpecialMessage(pinstance, "command_group_not_a_member", groupname.toUpperCase());

		}

		groupchatinfo = null;

	}

	public void displayHelp(int page, CommandSender sender) {

		if (page == 1) {

			MessageManager.sendMessage(sender, "groups_help_1");

		} else {

			MessageManager.sendMessage(sender, "groups_help_2");

		}
	}
}
