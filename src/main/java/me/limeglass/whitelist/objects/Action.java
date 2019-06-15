package me.limeglass.whitelist.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.reflect.Reflection;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Action {

	protected static Map<Action, String[]> modules = new HashMap<Action, String[]>();

	protected static void registerAction(Action action, String... actions) {
		if (!modules.containsValue(actions)) modules.put(action, actions);
	}

	public static void setup() {
		Reflections reflections = new Reflections("me.limeglass.whitelist");
		Set<Class<? extends Action>> classes = reflections.getSubTypesOf(Action.class);
		Reflection.initialize(classes.toArray(new Class[classes.size()]));
	}

	private static Action getAction(String action) {
		for (Entry<Action, String[]> entry : modules.entrySet()) {
			for (String subAction : entry.getValue()) {
				if (subAction.equals(action.toLowerCase())) {
					return entry.getKey();
				}
			}
		}
		return null;
	}

	@SafeVarargs
	public static void callAction(MessageReceivedEvent event, String command, String... parameters) {
		Action action = getAction(command);
		if (action != null)
			action.onActionCall(command, event, parameters);
	}

	public abstract void onActionCall(String action, MessageReceivedEvent event, String[] parameters);

}
