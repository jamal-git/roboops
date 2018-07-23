package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.commands.util.Command;
import com.oopsjpeg.roboops.util.Leaderboard;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class LeaderboardCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();

		Util.sendMessage(channel, "Showing a leaderboard for **" + author.getName() + "**.",
				Leaderboard.getGlobal(Leaderboard.MOST_MONEY));
	}

	@Override
	public String getName() {
		return "leaderboard";
	}

	@Override
	public String getDesc() {
		return "View the leaderboard.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"lb"};
	}
}
