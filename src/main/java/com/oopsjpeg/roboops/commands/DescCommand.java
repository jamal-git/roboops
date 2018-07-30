package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoboopsEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.UserWrapper;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DescCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		UserWrapper info = Roboops.getUser(author);

		if (args.length == 0)
			Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
					+ "the correct syntax is: `" + Roboops.getPrefix() + "desc <description>`");
		else if (args[0].equalsIgnoreCase("/clear")) {
			info.setDesc("");
			Bufferer.deleteMessage(message);
			Roboops.getMongo().saveUser(info);
			Bufferer.sendMessage(channel, RoboopsEmote.SUCCESS + "**" + author.getName() + "**, "
					+ "your description has been cleared.");
		} else {
			String desc = String.join(" ", args).trim();
			desc = desc.substring(0, Math.min(desc.length(), 250));
			info.setDesc(desc);
			Bufferer.deleteMessage(message);
			Roboops.getMongo().saveUser(info);
			Bufferer.sendMessage(channel, RoboopsEmote.SUCCESS + "**" + author.getName() + "**, "
					+ "your description has been updated.");
		}
	}

	@Override
	public String getName() {
		return "desc";
	}

	@Override
	public String getDesc() {
		return "Change your profile description.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"description"};
	}
}
