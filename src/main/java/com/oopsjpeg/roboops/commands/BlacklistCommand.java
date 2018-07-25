package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.framework.commands.CommandLevel;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class BlacklistCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IGuild guild = message.getGuild();
		IUser author = message.getAuthor();
	}

	@Override
	public String getName() {
		return "blacklist";
	}

	@Override
	public int getLevel() {
		return CommandLevel.BOT_OWNER;
	}
}
