package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.UserWrapper;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DailyCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		UserWrapper info = Roboops.getUser(author);

		if (!info.canDaily())
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "your daily bonus will be available in **" + info.dailyRemaining() + "**.");
		else {
			int daily = info.daily();
			Bufferer.deleteMessage(message);
			info.addMoney(daily);
			Roboops.getMongo().saveUser(info);
			Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "** "
					+ "has collected **$" + Util.comma(daily) + "** from their daily bonus!");
		}

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "daily";
	}

	@Override
	public String getDesc() {
		return "Collect your daily money.";
	}

	@Override
	public String getUsage() {
		return "[user]";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"d"};
	}
}
