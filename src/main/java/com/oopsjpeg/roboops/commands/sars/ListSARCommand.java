package com.oopsjpeg.roboops.commands.sars;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.GuildWrapper;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.stream.Collectors;

public class ListSARCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		GuildWrapper info = Roboops.getGuild(message.getGuild());

		if (info.getSelfRoles().isEmpty())
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "there are no self-assignable roles.");
		else {
			Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "**, "
					+ "the self-assignable roles are: "
					+ info.getSelfRoles().stream().map(IRole::getName).collect(Collectors.joining()));
		}

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "listsar";
	}

	@Override
	public String getDesc() {
		return "Lists the self-assignable roles.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"lsar"};
	}
}
