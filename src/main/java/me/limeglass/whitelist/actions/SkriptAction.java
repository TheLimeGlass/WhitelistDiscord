package me.limeglass.whitelist.actions;

import java.awt.Color;

import org.bukkit.Bukkit;

import ch.njol.skript.lang.Effect;
import me.limeglass.whitelist.WhitelistDiscord;
import me.limeglass.whitelist.objects.Action;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkriptAction extends Action {

	static {
		if (Bukkit.getPluginManager().isPluginEnabled("Skript"))
			registerAction(new SkriptAction(), "skript");
	}

	@Override
	public void onActionCall(String action, MessageReceivedEvent event, String[] parameters) {
		if (parameters == null)
			return;
		if (!WhitelistDiscord.getInstance().getConfig().getLongList("powerful-people").contains(event.getAuthor().getIdLong()))
			return;
		Bukkit.getScheduler().runTask(WhitelistDiscord.getInstance(), () -> {
			String entry = String.join(" ", parameters);
			Effect effect = Effect.parse(entry, null);
			EmbedBuilder builder = new EmbedBuilder()
					.setAuthor("Execute Skript Effect")
					.setColor(Color.YELLOW);
			if (effect == null) {
				builder.setColor(Color.RED);
				builder.appendDescription("Error executing effect `" + entry + "` ");
				builder.appendDescription("Maybe it's not a valid effect?");
			} else {
				effect.run(null);
				builder.appendDescription("Executing effect `" + entry + "`");
			}
			event.getChannel().sendMessageEmbeds(builder.build()).queue();
		});
	}

}
