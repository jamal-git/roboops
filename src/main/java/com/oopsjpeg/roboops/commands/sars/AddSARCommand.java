package com.oopsjpeg.roboops.commands.sars;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.GuildWrapper;
import sx.blah.discord.handle.obj.*;

import java.util.EnumSet;

public class AddSARCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		if (args.length == 0) return Command.INVALID_USAGE;

		IGuild guild = message.getGuild();
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		GuildWrapper info = Roboops.getGuild(guild);

		String query = String.join(" ", args);
		IRole role = guild.getRoles().stream()
				.filter(r -> r.getName().toLowerCase().contains(query.toLowerCase()))
				.findAny().orElse(null);

		if (role == null)
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "that role does not exist.");
		else if (info.getSelfRoles().contains(role))
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "that role is already self-assignable.");
		else {
			info.getSelfRoles().add(role);
			Roboops.getMongo().saveGuild(info);
			Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "** "
					+ "added **" + role.getName() + "** to the self-assignable roles.");
		}

		return Command.SUCCESS;
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
	public String getUsage() {
		return "<role>";
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
