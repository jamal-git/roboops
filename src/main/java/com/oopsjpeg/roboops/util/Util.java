package com.oopsjpeg.roboops.util;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.storage.User;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageTokenizer;
import sx.blah.discord.util.RequestBuffer;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Util {
	private static final Random random = new Random();

	public static Color getColor(IUser user, IGuild guild) {
		Color color = Color.LIGHT_GRAY;
		List<IRole> roles = user.getRolesForGuild(guild).stream()
				.sorted(Comparator.comparingInt(IRole::getPosition))
				.collect(Collectors.toList());

		for (IRole role : roles)
			if (!role.getColor().equals(new Color(0, 0, 0, 0)))
				color = role.getColor();

		return color;
	}

	public static EmbedObject getProfile(User user, IGuild guild) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorIcon(user.getAvatar());
		builder.withAuthorName(user.getName());

		if (user.getDesc() != null) builder.withDesc(user.getDesc());
		else builder.withDesc("Here is my profile!");

		if (guild != null) builder.withColor(Util.getColor(user.getUser(), guild));
		else builder.withColor(Color.LIGHT_GRAY);

		builder.appendField("Most Money", "$" + user.getMostMoney(), true);

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

	public static String timeDiff(LocalDateTime ldt1, LocalDateTime ldt2) {
		long hours = ldt1.until(ldt2, ChronoUnit.HOURS);
		ldt1 = ldt1.plusHours(hours);
		long minutes = ldt1.until(ldt2, ChronoUnit.MINUTES);

		return (hours > 0 ? hours + "h " : "") + minutes + "m";
	}

	public static IUser findUser(String[] args) {
		MessageTokenizer tokenizer = new MessageTokenizer(Roboops.getClient(), String.join(" ", args));

		if (tokenizer.hasNextMention())
			return (IUser) tokenizer.nextMention().getMentionObject();
		else return Roboops.getClient().getUsers().stream()
				.filter(u -> u.getName().equalsIgnoreCase(args[0])).findAny().orElse(null);
	}

	public static boolean randBool() {
		return random.nextBoolean();
	}

	public static int randInt(int max) {
		return random.nextInt(max + 1);
	}

	public static int randInt(int min, int max) {
		return min + random.nextInt((max + 1) - min);
	}

	public static float randFloat() {
		return random.nextFloat();
	}

	public static Color hex2Rgb(String colorStr) {
		return new Color(
				Integer.valueOf(colorStr.substring(1, 3), 16),
				Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public static int calcMaxXp(int level) {
		return (int) Math.round(Math.pow(level * 118, 1.07f) + 155);
	}

	public static void bulkDelete(IChannel channel, List<IMessage> messages) {
		RequestBuffer.request(() -> {
			if (channel.getModifiedPermissions(Roboops.getClient().getOurUser())
					.contains(Permissions.MANAGE_MESSAGES))
				channel.bulkDelete(messages);
		});
	}

	public static void deleteMessage(IMessage message) {
		RequestBuffer.request(() -> {
			if (message.getAuthor().equals(Roboops.getClient().getOurUser())
					|| message.getChannel().getModifiedPermissions(message.getAuthor())
					.contains(Permissions.MANAGE_MESSAGES))
				message.delete();
		});
	}

	public static void deleteMessage(int delay, IMessage message) {
		Roboops.SCHEDULER.schedule(() -> deleteMessage(message), delay, TimeUnit.SECONDS);
	}

	public static IMessage sendMessage(IChannel channel, String content) {
		return RequestBuffer.request(() -> {
			if (channel.getModifiedPermissions(Roboops.getClient().getOurUser())
					.contains(Permissions.SEND_MESSAGES))
				return channel.sendMessage(content);
			return null;
		}).get();
	}

	public static IMessage sendMessage(IChannel channel, EmbedObject embed) {
		return RequestBuffer.request(() -> {
			if (channel.getModifiedPermissions(Roboops.getClient().getOurUser())
					.contains(Permissions.SEND_MESSAGES))
				return channel.sendMessage(embed);
			return null;
		}).get();
	}

	public static IMessage sendMessage(IChannel channel, String content, EmbedObject embed) {
		return RequestBuffer.request(() -> {
			if (channel.getModifiedPermissions(Roboops.getClient().getOurUser())
					.contains(Permissions.SEND_MESSAGES))
				return channel.sendMessage(content, embed);
			return null;
		}).get();
	}

	public static IMessage sendFile(IChannel channel, File file) {
		return RequestBuffer.request(() -> {
			if (channel.getModifiedPermissions(Roboops.getClient().getOurUser())
					.containsAll(Arrays.asList(Permissions.SEND_MESSAGES, Permissions.ATTACH_FILES))) {
				try {
					return channel.sendFile(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return null;
		}).get();
	}

	public static IMessage editMessage(IMessage message, String content) {
		return RequestBuffer.request(() -> message.edit(content)).get();
	}
}
