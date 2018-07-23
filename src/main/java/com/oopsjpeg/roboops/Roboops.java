package com.oopsjpeg.roboops;

import com.oopsjpeg.roboops.commands.*;
import com.oopsjpeg.roboops.commands.util.Command;
import com.oopsjpeg.roboops.commands.util.CommandLevel;
import com.oopsjpeg.roboops.storage.User;
import com.oopsjpeg.roboops.util.Emote;
import com.oopsjpeg.roboops.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Roboops {
	public static final Logger LOGGER = LoggerFactory.getLogger(Roboops.class.getName());
	public static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(2);

	private static final List<Command> commands = new ArrayList<>();
	private static final List<User> users = new ArrayList<>();

	private static String token;
	private static String prefix;
	private static MongoMaster mongo;
	private static IDiscordClient client;

	public static void main(String[] args) {
		if (loadConfig()) {
			openMongo();
			client = new ClientBuilder().withToken(token).build();
			client.getDispatcher().registerListener(new Roboops());
			client.login();
		}
	}

	public static boolean loadConfig() {
		LOGGER.info("Reading configuration...");
		File file = new File("config.ini");
		Properties properties = new Properties();
		try {
			// Create the config file if it doesn't exist
			if (file.createNewFile()) try (FileWriter fw = new FileWriter(file)) {
				properties.setProperty("token", "");
				properties.setProperty("prefix", "r.");
				properties.store(fw, "roboops configuration");
			// Load properties file
			} else try (FileReader fr = new FileReader(file)) {
				properties.load(fr);
				token = properties.getProperty("token", "");
				prefix = properties.getProperty("prefix", "r.");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void openMongo() {
		LOGGER.info("Opening the mongo connection...");
		// Connect to the mongo server
		mongo = new MongoMaster();
		// Disconnect the mongo client when shutting down
		Runtime.getRuntime().addShutdownHook(new Thread(() -> mongo.close()));
		LOGGER.info("Successfully opened connection.");
	}

	public static MongoMaster getMongo() {
		return mongo;
	}

	public static String getPrefix() {
		return prefix;
	}

	public static IDiscordClient getClient() {
		return client;
	}

	public static List<Command> getCommands() {
		return commands;
	}

	public static Command getCommand(String alias) {
		return commands.stream()
				.filter(c -> c.getName().equalsIgnoreCase(alias) || Arrays.stream(c.getAliases())
						.anyMatch(s -> s.equalsIgnoreCase(alias)))
				.findAny().orElse(null);
	}

	public static void buildCommands() {
		commands.clear();
		commands.add(new BalanceCommand());
		commands.add(new CleanCommand());
		commands.add(new DailyCommand());
		commands.add(new DescCommand());
		commands.add(new GambleCommand());
		commands.add(new LeaderboardCommand());
		commands.add(new ProfileCommand());
	}

	public static List<User> getUsers() {
		return users;
	}

	public static User getUser(IUser user) {
		return users.stream().filter(u -> u.equals(user)).findAny().orElseGet(() -> {
			User u = new User(user);
			users.add(u);
			return u;
		});
	}

	@EventSubscriber
	public void onReady(ReadyEvent e) {
		mongo.loadUsers();
		buildCommands();
	}

	@EventSubscriber
	public void onMessage(MessageReceivedEvent e) {
		IMessage message = e.getMessage();
		IUser author = e.getAuthor();

		if (!author.isBot()) {
			IChannel channel = e.getChannel();
			String content = message.getContent();

			if (content.startsWith(prefix)) {
				String[] split = content.split(" ");
				String alias = split[0].replaceFirst(prefix, "");
				Command command = Roboops.getCommand(alias);

				if (command != null) {
					if (CommandLevel.get(author) < command.getLevel())
						Util.sendMessage(channel, Emote.ERROR + author + ", "
								+ "you are not a high enough permission level for that command.");
					else if (!channel.getModifiedPermissions(author).containsAll(command.getPermissions()))
						Util.sendMessage(channel, Emote.ERROR + author + ", "
								+ "you do not have permission(s) for that command.");
					else {
						System.out.println(author.getName() + "#" + author.getDiscriminator() + ": " + content);
						String[] args = Arrays.copyOfRange(split, 1, split.length);
						command.execute(message, alias, args);
					}
				}
			}
		}
	}
}
