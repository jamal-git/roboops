package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.commands.util.Command;
import com.oopsjpeg.roboops.storage.User;
import com.oopsjpeg.roboops.util.Emote;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DailyCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		User info = Roboops.getUser(author);

		if (!info.canDaily())
			Util.sendMessage(channel, Emote.ERROR + "**" + author.getName() + "**, "
					+ "your daily bonus will be available in **" + info.dailyRemaining() + "**.");
		else {
			int daily = info.daily();
			info.addMoney(daily);
			Roboops.getMongo().saveUser(author);
			Util.sendMessage(channel, Emote.SUCCESS + "**" + author.getName() + "** "
					+ "has collected **$" + Util.comma(daily) + "** from their daily bonus!");
		}

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
	public String[] getAliases() {
		return new String[]{"d"};
	}
}
