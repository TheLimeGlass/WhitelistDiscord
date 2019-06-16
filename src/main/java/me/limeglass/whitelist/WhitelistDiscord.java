package me.limeglass.whitelist;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

public class WhitelistDiscord extends JavaPlugin {

	private Properties properties = new Properties();
	private static WhitelistDiscord instance;
	private static JDA client;
	private long channel;

	public void onEnable() {
		instance = this;
		Action.setup();
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			if (inputStream != null)
				properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		channel = Long.parseLong(properties.getProperty("discord.channel"));
		try {
			client = new JDABuilder(properties.getProperty("client.token"))
			        .addEventListener(new DiscordListener())
			        .build();
			client.awaitReady();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
			return;
		}
		Bukkit.getPluginManager().registerEvents(new SpigotListener(channel), this);
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**Server is Online!**")
				.setColor(Color.WHITE)
				.build();
		new MessageBuilder()
				.sendTo(client.getTextChannelById(channel))
				.embed(embed)
				.submit(true);
		Bukkit.getLogger().info("WhitelistDiscord has been enabled!");
	}

	public void onDisable() {
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**Server Shutting down...**")
				.setColor(Color.BLACK)
				.build();
		new MessageBuilder()
				.sendTo(client.getTextChannelById(channel))
				.embed(embed)
				.submit(true);
		client.shutdown();
	}

	public static WhitelistDiscord getInstance() {
		return instance;
	}

	public static JDA getClient() {
		return client;
	}

	public long getChannel() {
		return channel;
	}

}
