package com.oopsjpeg.roboops.util;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.storage.User;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Leaderboard {
	public static final int MOST_MONEY = 0;

	public static EmbedObject getGlobal(int mode) {
		List<String> board;

		switch (mode) {
			default: board = mostMoney(Roboops.getUsers());
		}

		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField(Emoji.STAR2 + "" + modeName(mode) + " (Global)",
				String.join("\n", board), false);

		return builder.build();
	}

	public static String modeName(int mode) {
		switch (mode) {
			default: return "Most Money";
		}
	}

	public static List<String> mostMoney(List<User> users) {
		AtomicInteger position = new AtomicInteger(10);
		List<String> board = users.stream()
				.sorted(Comparator.comparingInt(User::getMostMoney)).limit(10)
				.map(u -> "**#" + position.getAndDecrement() + "** - " + u.getName() + "#"
						+ u.getDiscriminator() + " with **$" + Util.comma(u.getMostMoney()) + "**")
				.collect(Collectors.toList());
		Collections.reverse(board);

		return board;
	}
}
