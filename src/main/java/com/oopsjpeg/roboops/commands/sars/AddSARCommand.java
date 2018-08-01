package com.oopsjpeg.roboops.commands.sars;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoboopsEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.GuildWrapper;
import sx.blah.discord.handle.obj.*;

import java.util.EnumSet;

public class AddSARCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IGuild guild = message.getGuild();
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();

		if (args.length == 0)
			Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
					+ "the correct syntax is: `" + Roboops.getPrefix() + "addsar <name>`");
		else {
			GuildWrapper info = Roboops.getGuild(guild);
			String query = String.join(" ", args);
			IRole role = guild.getRoles().stream()
					.filter(r -> r.getName().toLowerCase().contains(query.toLowerCase()))
					.findAny().orElse(null);

			if (role == null)
				Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
						+ "that role does not exist.");
			else if (info.getSelfRoles().contains(role))
				Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
						+ "that role is already self-assignable.");
			else {
				Bufferer.deleteMessage(message);
				info.getSelfRoles().add(role);
				Roboops.getMongo().saveGuild(info);
				Bufferer.sendMessage(channel, RoboopsEmote.SUCCESS + "**" + author.getName() + "** "
						+ "added **" + role.getName() + "** to the self-assignable roles.");
			}
		}
	}

	@Override
	public String getName() {
		return "addsar";
	}

	@Override
	public String getDesc() {
		return "Add a role to the self-assignable roles.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"asar"};
	}

	@Override
	public EnumSet<Permissions> getPermissions() {
		return EnumSet.of(Permissions.MANAGE_ROLES);
	}
}
