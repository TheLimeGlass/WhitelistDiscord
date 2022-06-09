package me.limeglass.whitelist.listeners;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Sets;

import me.limeglass.whitelist.WhitelistDiscord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class SpigotListener implements Listener {

	private final static Set<OfflinePlayer> players = new HashSet<>();
	private final Set<TextChannel> channels = new HashSet<>();
	private final WhitelistDiscord instance;
	public static final int minutes = 15;

	public SpigotListener(WhitelistDiscord instance, Set<TextChannel> channels) {
		this.channels.addAll(channels);
		this.instance = instance;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		players.add(event.getPlayer());
		Bukkit.getScheduler().runTaskLaterAsynchronously(instance, new Runnable() {
			@Override
			public void run() {
				players.remove(event.getPlayer());
			}
		}, (20 * 60) * minutes);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**" + player.getName() + "** joined the server")
				.setColor(Color.GREEN)
				.build();
		for (TextChannel channel : channels) {
			channel.getManager().setTopic("**" + instance.getConfig().getString("address", "example.server.com") + "** - Online players: " + Bukkit.getOnlinePlayers().size()).queue();
			channel.sendMessageEmbeds(embed).queue();
		}
		if (!player.hasPlayedBefore() && instance.getConfig().getBoolean("gift-login-rewards", true))
			player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 25), new ItemStack(Material.TORCH, 10));
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		try {
			MessageEmbed embed = new EmbedBuilder()
					.appendDescription("**" + event.getPlayer().getName() + "** left the server")
					.setColor(Color.RED)
					.build();
			for (TextChannel channel : channels) {
				channel.getManager().setTopic("**" + WhitelistDiscord.getInstance().getConfig().getString("address", "example.server.com") + "** - Online players: " + (Bukkit.getOnlinePlayers().size() - 1)).queue();
				channel.sendMessageEmbeds(embed).queue();
			}
		} catch (IllegalStateException e) {
			Set<TextChannel> clone = Sets.newHashSet(channels);
			channels.clear();
			clone.forEach(channel -> channels.add(instance.getClient().getTextChannelById(channel.getIdLong())));
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Location location = event.getEntity().getLocation();
		String format = location.getX() + ", " + location.getY() + ", " + location.getZ();
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**" + event.getDeathMessage() + " at " + format + "**")
				.setColor(Color.BLUE)
				.build();
		for (TextChannel channel : channels)
			channel.sendMessageEmbeds(embed).queue();
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String string = event.getMessage();
		if (string.startsWith("\\"))
			return;
		for (TextChannel channel : channels)
			channel.sendMessage("`" + event.getPlayer().getName() + "`: " + string).queue();
	}

	public static boolean hasJoinedRecently(OfflinePlayer player) {
		return players.contains(player);
	}

}
