package com.oopsjpeg.roboops.commands.util;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.util.EnumSet;

public interface Command {
	void execute(IMessage message, String alias, String[] args);

	String getName();

	default String getDesc() {
		return "";
	}

	default String[] getAliases() {
		return new String[0];
	}

	default EnumSet<Permissions> getPermissions() {
		return EnumSet.noneOf(Permissions.class);
	}

	default int getLevel() {
		return CommandLevel.USER;
	}
}
