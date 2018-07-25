package com.oopsjpeg.roboops.util;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Emote;
import com.oopsjpeg.roboops.storage.User;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Leaderboard {
	public static final int MOST_MONEY = 0;

	public static EmbedObject get(int mode, String scope, Collection<User> users) {
		List<String> board;

		switch (mode) {
			default: board = mostMoney(users);
		}

		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField(Emote.STAR2 + modeName(mode) + " (" + scope + ")",
				String.join("\n", board), false);

		return builder.build();
	}

	public static EmbedObject getGlobal(int mode) {
		return get(mode, "Global", Roboops.getUsers());
	}

	public static EmbedObject getGuild(int mode, IGuild guild) {
		return get(mode, guild.getName(), convertUsers(guild.getUsers()));
	}

	public static String modeName(int mode) {
		switch (mode) {
			default: return "Most Money";
		}
	}

	public static List<String> mostMoney(Collection<User> users) {
		AtomicInteger position = new AtomicInteger();
		return users.stream()
				.sorted((u1, u2) -> Integer.compare(u2.getMostMoney(), u1.getMostMoney())).limit(10)
				.map(u -> "**#" + position.incrementAndGet() + "** - " + u.getName() + "#"
						+ u.getDiscriminator() + " with **$" + Util.comma(u.getMostMoney()) + "**")
				.collect(Collectors.toList());
	}

	private static List<User> convertUsers(Collection<IUser> users) {
		return users.stream()
				.filter(Roboops.getUsers()::contains)
				.map(Roboops::getUser)
				.collect(Collectors.toList());
	}
}
