package com.oopsjpeg.roboops.storage;

import com.oopsjpeg.roboops.Roboops;
import sx.blah.discord.handle.obj.IGuild;

public class GuildWrapper {
	private final long id;

	private Blacklist blacklist = new Blacklist();

	public GuildWrapper(IGuild guild) {
		this.id = guild.getLongID();
	}

	public long getID() {
		return id;
	}

	public IGuild getGuild() {
		return Roboops.getClient().getGuildByID(id);
	}

	public Blacklist getBlacklist() {
		return blacklist;
	}

	public class Blacklist {

	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& ((obj instanceof GuildWrapper && ((GuildWrapper) obj).id == id)
				|| (obj instanceof IGuild) && getGuild().equals(obj));
	}
}
