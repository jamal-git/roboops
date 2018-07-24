package com.oopsjpeg.roboops.storage;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;
import java.util.List;

public class Guild {
	private final IGuild guild;

	private List<IChannel> blacklistChannels = new ArrayList<>();

	public Guild(IGuild guild) {
		this.guild = guild;
	}

	public IGuild getGuild() {
		return guild;
	}

	public long getID() {
		return guild.getLongID();
	}

	public List<IChannel> getBlacklistChannels() {
		return blacklistChannels;
	}

	public void setBlacklistChannels(List<IChannel> blacklistChannels) {
		this.blacklistChannels = blacklistChannels;
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
