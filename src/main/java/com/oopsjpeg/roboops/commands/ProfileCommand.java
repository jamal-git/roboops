package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class ProfileCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		Util.sendMessage(channel, "Showing **" + author.getName() + "**'s profile.",
				Util.getProfile(author, message.getGuild()));
	}

	@Override
	public String getName() {
		return "profile";
	}

	@Override
	public String getDesc() {
		return "View your profile.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"p"};
	}
}
