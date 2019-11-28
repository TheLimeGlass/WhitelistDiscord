package me.limeglass.whitelist.actions;

import java.awt.Color;

import me.limeglass.whitelist.WhitelistDiscord;
import me.limeglass.whitelist.objects.Action;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpAction extends Action {

	static {
		registerAction(new HelpAction(), "help");
	}

	@Override
	public void onActionCall(String action, MessageReceivedEvent event, String[] parameters) {
		SelfUser bot = WhitelistDiscord.getClient().getSelfUser();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("Whitelist Discord Help Menu", "http://pscube.com/wp-content/uploads/2017/06/24.jpg");
		builder.addField("Bot Information", "Whitelist Discord is a bot that allows users to whitelist themselves on a Minecraft server from the usage of the bot commands."
				+ "\n**Links:** [Source code](https://github.com/TheLimeGlass/WhitelistDiscord)"
				+ "\n**Prefixes:** " + bot.getAsMention()
				+ "               [] = optional arguments. () = required arguments.", false);
		builder.addField("help", "Messages the help menu.", false);
		builder.addField("whitelist (user)",
				"Command for users to whitelist themselves on the Minecraft server."
				+ "\n> *Example:* " + bot.getAsMention() + " `whitelist LimeGlass`", false);
		builder.addField("Server IP", "**" + WhitelistDiscord.getInstance().getConfig().getString("address", "example.server.com") + "**", false);
		builder.setColor(Color.GREEN);
		new MessageBuilder()
				.sendTo(event.getChannel())
				.embed(builder.build())
				.submit(true);
	}

}
