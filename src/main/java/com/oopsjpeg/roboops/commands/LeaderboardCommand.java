package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.util.Leaderboard;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class LeaderboardCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IGuild guild = message.getGuild();
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();

		EmbedObject embed;

		if (args.length >= 1 && args[0].equalsIgnoreCase("server"))
			embed = Leaderboard.getGuild(Leaderboard.MOST_MONEY, guild);
		else
			embed = Leaderboard.getGlobal(Leaderboard.MOST_MONEY);

		Bufferer.sendMessage(channel, "Showing a leaderboard for **" + author.getName() + "**.", embed);
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
