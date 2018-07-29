package com.oopsjpeg.roboops.util;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.RoboopsEmote;
import com.oopsjpeg.roboops.storage.UserWrapper;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Leaderboard {
	public static EmbedObject get(Mode mode, String scope, List<UserWrapper> users) {
		List<String> board = mode.get(users);

		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField(RoboopsEmote.STAR2 + mode.getName() + " (" + scope + ")",
				String.join("\n", board), false);

		return builder.build();
	}

	public static EmbedObject convert(Mode mode, String scope, List<IUser> users) {
		return get(mode, scope, users.stream().map(Roboops::getUser).collect(Collectors.toList()));
	}

	public enum Mode {
		MOST_MONEY("Most Money") {
			@Override
			public List<String> get(List<UserWrapper> users) {
				AtomicInteger position = new AtomicInteger();
				return users.stream()
						.sorted((u1, u2) -> Integer.compare(u2.getMostMoney(), u1.getMostMoney())).limit(10)
						.map(u -> "**#" + position.incrementAndGet() + "** - " + u.getUser().getName() + "#"
								+ u.getUser().getDiscriminator() + " with **$" + Util.comma(u.getMostMoney()) + "**")
						.collect(Collectors.toList());
			}
		};

		private final String name;

		Mode(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public abstract List<String> get(List<UserWrapper> users);
	}
}
