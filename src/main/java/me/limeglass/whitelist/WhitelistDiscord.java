package me.limeglass.whitelist;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.limeglass.whitelist.listeners.DiscordListener;
import me.limeglass.whitelist.listeners.SpigotListener;
import me.limeglass.whitelist.objects.Action;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

public class WhitelistDiscord extends JavaPlugin {

	private Set<TextChannel> channels = new HashSet<>();
	private static WhitelistDiscord instance;
	private static JDA client;

	public void onEnable() {
		instance = this;
		Action.setup();
		saveDefaultConfig();
		if (!getConfig().isSet("client-token"))
			return;
		try {
			client = new JDABuilder(getConfig().getString("client-token"))
			        .addEventListener(new DiscordListener())
			        .build();
			client.awaitReady();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
			return;
		}
		for (long id : getConfig().getLongList("discord-channels")) {
			if (id <= 0)
				continue;
			TextChannel channel = client.getTextChannelById(id);
			if (channel == null)
				continue;
			channels.add(channel);
			System.out.println("Added channel " + id + " " + channel.getName());
		}
		Bukkit.getPluginManager().registerEvents(new SpigotListener(channels), this);
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**Server is Online!**")
				.setColor(Color.WHITE)
				.build();
		channels.removeIf(channel -> channel == null);
		for (TextChannel channel : channels)
			new MessageBuilder()
					.sendTo(channel)
					.embed(embed)
					.submit(true);
		Bukkit.getLogger().info("WhitelistDiscord has been enabled!");
	}

	public void onDisable() {
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**Server Shutting down...**")
				.setColor(Color.BLACK)
				.build();
		for (TextChannel channel : channels) {
			channel.getManager().setTopic("**" + WhitelistDiscord.getInstance().getConfig().getString("address", "example.server.com") + "** - Online players: 0").queue();
			new MessageBuilder()
					.sendTo(channel)
					.embed(embed)
					.submit(true);
		}
		client.shutdown();
	}

	public static WhitelistDiscord getInstance() {
		return instance;
	}

	public static JDA getClient() {
		return client;
	}

	public Set<TextChannel> getChannels() {
		return channels;
	}

}
