package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.commands.util.Command;
import com.oopsjpeg.roboops.util.Emoji;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageHistory;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CleanCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		IUser bot = Roboops.getClient().getOurUser();

		if (channel.isPrivate())
			Util.sendMessage(channel, Emoji.ERROR + "**" + author.getName() + "**, "
					+ "this command can only be done in servers.");
		else {
			MessageHistory history = channel.getMessageHistory(100);
			List<IMessage> messages = new LinkedList<>();
			// Remove if sent by the bot
			messages.addAll(history.stream()
					.filter(m -> m.getAuthor().equals(bot))
					.collect(Collectors.toList()));
			// Remove if message starts with prefix (if allowed)
			if (channel.getModifiedPermissions(bot).contains(Permissions.MANAGE_MESSAGES))
				messages.addAll(history.stream()
					.filter(m -> m.getContent().startsWith(Roboops.getPrefix()))
					.collect(Collectors.toList()));
			// Delete the collected messages
			channel.bulkDelete(messages);

			Util.sendMessage(channel, Emoji.SUCCESS + "**" + author.getName() + "** "
					+ "cleared **" + messages.size() + "** message(s).");
		}
	}

	@Override
	public String getName() {
		return "clean";
	}

	@Override
	public String getDesc() {
		return "Clean certain messages from the channel.";
	}

	@Override
	public EnumSet<Permissions> getPermissions() {
		return EnumSet.of(Permissions.MANAGE_MESSAGES);
	}
}
