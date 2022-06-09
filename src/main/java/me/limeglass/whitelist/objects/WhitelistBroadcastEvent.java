package me.limeglass.whitelist.objects;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WhitelistBroadcastEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private String message;

	public WhitelistBroadcastEvent(String message) {
		super(true);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
