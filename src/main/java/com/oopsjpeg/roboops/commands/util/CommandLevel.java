package com.oopsjpeg.roboops.commands.util;

import com.oopsjpeg.roboops.Roboops;
import sx.blah.discord.handle.obj.IUser;

public class CommandLevel {
	public static final int USER = 0;
	public static final int BOT_OWNER = 1;

	public static int get(IUser user) {
		if (user.equals(Roboops.getClient().getApplicationOwner()))
			return BOT_OWNER;
		return USER;
	}
}
