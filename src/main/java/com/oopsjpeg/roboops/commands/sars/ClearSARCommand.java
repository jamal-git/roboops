package com.oopsjpeg.roboops.commands.sars;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.GuildWrapper;
import sx.blah.discord.handle.obj.*;

import java.util.EnumSet;

public class ClearSARCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		GuildWrapper info = Roboops.getGuild(message.getGuild());

		if (info.getSelfRoles().isEmpty())
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "there are no self-assignable roles.");
		else {
			Bufferer.deleteMessage(message);
			info.getSelfRoles().clear();
			Roboops.getMongo().saveGuild(info);
			Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "** "
					+ "cleared all self-assignable roles.");
		}

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "clearsar";
	}

	@Override
	public String getDesc() {
		return "Clear the self-assignable roles.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"csar"};
	}

	@Override
	public EnumSet<Permissions> getPermissions() {
		return EnumSet.of(Permissions.MANAGE_ROLES);
	}
}
