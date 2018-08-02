package com.oopsjpeg.roboops.commands.sars;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.GuildWrapper;
import sx.blah.discord.handle.obj.*;

public class RoleCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		if (args.length == 0) return INVALID_USAGE;

		IGuild guild = message.getGuild();
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		GuildWrapper info = Roboops.getGuild(message.getGuild());

		String query = String.join(" ", args);
		IRole role = guild.getRoles().stream()
				.filter(r -> r.getName().toLowerCase().contains(query.toLowerCase()))
				.findAny().orElse(null);

		if (role == null)
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "that role does not exist.");
		else if (!info.getSelfRoles().contains(role))
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "that role is not self-assignable.");
		else {
			Bufferer.deleteMessage(message);
			if (!author.getRolesForGuild(guild).contains(role)) {
				author.addRole(role);
				Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "**, "
						+ "you now have **" + role.getName() + "**.");
			} else {
				author.removeRole(role);
				Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "**, "
						+ "you no longer have **" + role.getName() + "**.");
			}
			Roboops.getMongo().saveUser(Roboops.getUser(author));
		}

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "role";
	}

	@Override
	public String getDesc() {
		return "Toggles a self-assignable role.";
	}
}
