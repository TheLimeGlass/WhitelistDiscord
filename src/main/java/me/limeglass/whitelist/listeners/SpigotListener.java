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

import me.limeglass.whitelist.WhitelistDiscord;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class SpigotListener implements Listener {

	private final static Set<OfflinePlayer> players = new HashSet<>();
	public static final int minutes = 15;
	private final long channel;

	public SpigotListener(long channel) {
		this.channel = channel;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		players.add(event.getPlayer());
		Bukkit.getScheduler().runTaskLaterAsynchronously(WhitelistDiscord.getInstance(), new Runnable() {
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
		new MessageBuilder()
				.sendTo(WhitelistDiscord.getClient().getTextChannelById(channel))
				.embed(embed)
				.submit(true);
		if (!player.hasPlayedBefore()) {
			Set<ItemStack> gifts = new HashSet<>();
			gifts.add(new ItemStack(Material.GOLDEN_CARROT, 25));
			gifts.add(new ItemStack(Material.TORCH, 10));
			player.getInventory().addItem(gifts.toArray(new ItemStack[gifts.size()]));
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**" + event.getPlayer().getName() + "** left the server")
				.setColor(Color.RED)
				.build();
		new MessageBuilder()
				.sendTo(WhitelistDiscord.getClient().getTextChannelById(channel))
				.embed(embed)
				.submit(true);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Location location = event.getEntity().getLocation();
		String format = location.getX() + ", " + location.getY() + ", " + location.getZ();
		MessageEmbed embed = new EmbedBuilder()
				.appendDescription("**" + event.getDeathMessage() + " at " + format + "**")
				.setColor(Color.BLUE)
				.build();
		new MessageBuilder()
				.sendTo(WhitelistDiscord.getClient().getTextChannelById(channel))
				.embed(embed)
				.submit(true);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String string = event.getMessage();
		if (string.startsWith("\\"))
			return;
		new MessageBuilder()
				.sendTo(WhitelistDiscord.getClient().getTextChannelById(channel))
				.content("`" + event.getPlayer().getName() + "`: " + string)
				.submit(true);
	}

	public static boolean hasJoinedRecently(OfflinePlayer player) {
		return players.contains(player);
	}

}
