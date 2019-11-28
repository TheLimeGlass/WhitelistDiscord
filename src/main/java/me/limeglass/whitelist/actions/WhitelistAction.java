package me.limeglass.whitelist.actions;

import java.awt.Color;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Sets;

import me.limeglass.whitelist.WhitelistDiscord;
import me.limeglass.whitelist.listeners.SpigotListener;
import me.limeglass.whitelist.objects.Action;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WhitelistAction extends Action {

	static {
		registerAction(new WhitelistAction(), "whitelist", "add");
	}

	@Override
	public void onActionCall(String action, MessageReceivedEvent event, String[] parameters) {
		if (parameters == null)
			return;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setAuthor("Whitelisted Status");
		User user = event.getAuthor();
		FileConfiguration configuration = WhitelistDiscord.getInstance().getConfig();
		for (String parameter : parameters) {
			if (!parameter.matches("\\w+")) {
				builder.appendDescription(":x: **" + parameter + "** " + " incorrect username\n");
				continue;
			}
			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(parameter);
			if (Bukkit.getWhitelistedPlayers().contains(player)) {
				builder.appendDescription(":x: **" + parameter + "** " + " user already whitelisted\n");
				continue;
			}
			if (!SpigotListener.hasJoinedRecently(player)) {
				builder.appendDescription(":x: **" + parameter + "** " + " user has not attempted to join the server in the past " + SpigotListener.minutes + " minutes\n");
				continue;
			}
			player.setWhitelisted(true);
			if (!player.isWhitelisted()) {
				builder.appendDescription(":x: **" + parameter + "** " + " something went wrong, the user did not get whitelisted\n");
				continue;
			}
			configuration.set(user.getId() + "." + "name", user.getName());
			if (configuration.isSet(user.getId() + "." + "whitlisted")) {
				List<String> whitelisted = configuration.getStringList(user.getId() + "." + "whitlisted");
				whitelisted.add(player.getName() + " " + player.getUniqueId());
				configuration.set(user.getId() + "." + "whitlisted", whitelisted);
			} else {
				configuration.set(user.getId() + "." + "whitlisted", Sets.newHashSet(user.getName()));
			}
			builder.appendDescription(":white_check_mark: **" + parameter + "** " + " whitelisted\n");
		}
		new MessageBuilder()
				.sendTo(event.getChannel())
				.embed(builder.build())
				.submit(true);
	}

}
