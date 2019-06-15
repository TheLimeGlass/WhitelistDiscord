package me.limeglass.whitelist.objects;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Command {

	private final MessageReceivedEvent event;
	private String[] arguments;
	private String command;

	public Command(MessageReceivedEvent event, String command, String... arguments) {
		this.event = event;
		this.command = command;
		this.arguments = arguments;
		Action.callAction(event, command, arguments);
	}

	public MessageReceivedEvent getEvent() {
		return event;
	}

	public String[] getArguments() {
		return arguments;
	}

	public String getCommand() {
		return command;
	}

}
