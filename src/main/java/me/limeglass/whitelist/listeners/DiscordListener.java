package me.limeglass.whitelist.listeners;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.limeglass.whitelist.WhitelistDiscord;
import me.limeglass.whitelist.objects.Command;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class DiscordListener implements EventListener {

	@Override
	public void onEvent(Event event) {
		JDA client = WhitelistDiscord.getClient();
		if (event instanceof ReadyEvent)
			client.getPresence().setPresence(OnlineStatus.ONLINE, Game.listening("mention me"));
		else if (event instanceof MessageReceivedEvent) {
			MessageReceivedEvent messageEvent = (MessageReceivedEvent) event;
			if (!WhitelistDiscord.getInstance().getConfig().getLongList("discord-channels").contains(messageEvent.getChannel().getIdLong()))
				return;
			if (messageEvent.getAuthor().getIdLong() == client.getSelfUser().getIdLong())
				return;
			String content = messageEvent.getMessage().getContentDisplay();
			if (content.toLowerCase().startsWith("@" + client.getSelfUser().getName().toLowerCase())) {
				if (content.equalsIgnoreCase("@" + client.getSelfUser().getName())) {
					new Command(messageEvent, "help");
					return;
				}
				String[] split = content.split(" ");
				if (split.length <= 2) {
					new Command(messageEvent, "help");
					return;
				}
				String command = split[1];
				String[] arguments = Arrays.copyOfRange(split, 2, split.length);
				new Command(messageEvent, command, arguments);
				return;
			}
			Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + ChatColor.BLUE + messageEvent.getAuthor().getName() + ChatColor.YELLOW + "]" + ChatColor.GRAY + ": " + content);
		}
	}

}
