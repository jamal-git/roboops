package com.oopsjpeg.roboops.util;

import com.oopsjpeg.roboops.framework.RoboopsUtil;
import com.oopsjpeg.roboops.storage.UserWrapper;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.Properties;
import java.util.Random;

public class Util extends RoboopsUtil {
	private static final Random random = new Random();
	private static final Properties defConfig = new Properties();

	static {
		defConfig.put("token", "");
		defConfig.put("prefix", "r.");
	}

	public static EmbedObject getProfile(UserWrapper info, IGuild guild) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorIcon(info.getUser().getAvatar());
		builder.withAuthorName(info.getUser().getName());

		if (info.getDesc() != null) builder.withDesc(info.getDesc());
		else builder.withDesc("Here is my profile!");

		if (guild != null) builder.withColor(getColor(info.getUser(), guild));
		else builder.withColor(Color.LIGHT_GRAY);

		builder.appendField("Most Money", "$" + comma(info.getMostMoney()), true);

		return builder.build();
	}

	public static EmbedObject getProfile(UserWrapper user) {
		return getProfile(user, null);
	}

	public static Properties getDefaultConfig() {
		return defConfig;
	}

	public static String getProperty(Properties initProp, Properties newProp, String key) {
		if (initProp.containsKey(key)) {
			return initProp.getProperty(key);
		} else newProp.put(key, defConfig.getProperty(key, ""));
		return defConfig.getProperty(key, "");
	}
}
