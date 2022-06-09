package me.limeglass.whitelist.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.limeglass.whitelist.WhitelistDiscord;
import me.limeglass.whitelist.objects.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {

	private final WhitelistDiscord instance;

	public DiscordListener(WhitelistDiscord instance) {
		this.instance = instance;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		JDA client = instance.getClient();
		if (!WhitelistDiscord.getInstance().getConfig().getLongList("discord-channels").contains(event.getChannel().getIdLong()))
			return;
		if (event.getAuthor().getIdLong() == client.getSelfUser().getIdLong())
			return;
		String content = event.getMessage().getContentDisplay();
		if (content.toLowerCase().startsWith("@" + client.getSelfUser().getName().toLowerCase())) {
			if (content.equalsIgnoreCase("@" + client.getSelfUser().getName())) {
				new Command(event, "help");
				return;
			}
			String[] split = content.split(" ");
			if (split.length <= 2) {
				new Command(event, "help");
				return;
			}
			String command = split[1];
			String[] arguments = Arrays.copyOfRange(split, 2, split.length);
			new Command(event, command, arguments);
			return;
		}
		Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.BLUE + event.getAuthor().getName() + ChatColor.YELLOW + "]" + ChatColor.GRAY + ": " + content);
	}

}
