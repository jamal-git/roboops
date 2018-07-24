package com.oopsjpeg.roboops.storage;

import sx.blah.discord.handle.obj.IGuild;

public class Guild {
	private final IGuild guild;

	public Guild(IGuild guild) {
		this.guild = guild;
	}

	public IGuild getGuild() {
		return guild;
	}

	public long getID() {
		return guild.getLongID();
	}

	@Override
	public boolean equals(Object obj) {
		return guild.equals(obj);
	}

	@Override
	public String toString() {
		return guild.toString();
	}
}
