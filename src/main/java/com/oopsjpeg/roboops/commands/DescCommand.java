package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.UserWrapper;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DescCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		if (args.length == 0) return INVALID_USAGE;

		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		UserWrapper info = Roboops.getUser(author);

		if (args[0].equalsIgnoreCase("/reset")) {
			info.setDesc("");
			Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "**, "
					+ "your description has been reset.");
		} else {
			String desc = String.join(" ", args).trim();
			desc = desc.substring(0, Math.min(desc.length(), 250));
			info.setDesc(desc);
			Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "**, "
					+ "your description has been updated.");
		}

		Roboops.getMongo().saveUser(info);

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "desc";
	}

	@Override
	public String getDesc() {
		return "Change your profile's description.";
	}

	@Override
	public String getUsage() {
		return "[\"/reset\"]";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"description"};
	}
}
