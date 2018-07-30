package com.oopsjpeg.roboops.storage;

import com.oopsjpeg.roboops.Roboops;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

import java.util.ArrayList;
import java.util.List;

public class GuildWrapper {
	private final long id;

	private Blacklist blacklist = new Blacklist();
	private List<IRole> selfRoles = new ArrayList<>();

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

	public List<IRole> getSelfRoles() {
		return selfRoles;
	}

	public void setSelfRoles(List<IRole> selfRoles) {
		this.selfRoles = selfRoles;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& ((obj instanceof GuildWrapper && ((GuildWrapper) obj).id == id)
				|| (obj instanceof IGuild) && getGuild().equals(obj));
	}

	public class Blacklist {

	}
}
