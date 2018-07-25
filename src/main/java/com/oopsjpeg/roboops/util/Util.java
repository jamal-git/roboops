package com.oopsjpeg.roboops.util;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.RoboopsUtil;
import com.oopsjpeg.roboops.storage.User;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.Random;

public class Util extends RoboopsUtil {
	private static final Random random = new Random();

	public static EmbedObject getProfile(User user, IGuild guild) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorIcon(user.getAvatar());
		builder.withAuthorName(user.getName());

		if (user.getDesc() != null) builder.withDesc(user.getDesc());
		else builder.withDesc("Here is my profile!");

		if (guild != null) builder.withColor(getColor(user.getUser(), guild));
		else builder.withColor(Color.LIGHT_GRAY);

		builder.appendField("Most Money", "$" + comma(user.getMostMoney()), true);

		return builder.build();
	}

	public static EmbedObject getProfile(User user) {
		return getProfile(user, null);
	}

	public static EmbedObject getProfile(IUser user, IGuild guild) {
		return getProfile(Roboops.getUser(user), guild);
	}

	public static EmbedObject getProfile(IUser user) {
		return getProfile(Roboops.getUser(user), null);
	}
}
