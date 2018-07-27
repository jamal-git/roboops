package com.oopsjpeg.roboops;

import com.oopsjpeg.roboops.commands.*;
import com.oopsjpeg.roboops.framework.RoboopsTray;
import com.oopsjpeg.roboops.framework.commands.CommandCenter;
import com.oopsjpeg.roboops.storage.Guild;
import com.oopsjpeg.roboops.storage.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Roboops {
	public static final Logger LOGGER = LoggerFactory.getLogger(Roboops.class.getName());
	public static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(2);

	private static final CommandCenter commands = new CommandCenter();
	private static final List<User> users = new ArrayList<>();
	private static final List<Guild> guilds = new ArrayList<>();

	private static String token;
	private static String prefix;

	private static RoboopsTray tray = new RoboopsTray("roboops");
	private static MongoMaster mongo;
	private static IDiscordClient client;

	public static void main(String[] args) {
		if (loadConfig()) {
			openMongo();
			commands.setPrefix(prefix);
			client = new ClientBuilder().withToken(token).build();
			client.getDispatcher().registerListener(new Roboops());
			client.getDispatcher().registerListener(commands);
			client.login();
		}
	}

	public static boolean loadConfig() {
		LOGGER.info("Reading configuration...");
		File file = new File("config.ini");
		Properties properties = new Properties();
		try {
			if (!file.exists()) {
				try (FileWriter fw = new FileWriter(file)) {
					// Create the config file if it doesn't exist
					properties.setProperty("token", "");
					properties.setProperty("prefix", "r.");
					properties.store(fw, "roboops configuration");
				}
			} else {
				try (FileReader fr = new FileReader(file)) {
					// Load properties file
					properties.load(fr);
					token = properties.getProperty("token", "");
					prefix = properties.getProperty("prefix", "r.");
					return true;
				}
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

	public static CommandCenter getCommands() {
		return commands;
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
		if (!users.contains(user))
			users.add(new User(user));
		return users.get(users.indexOf(user));
	}

	public static List<Guild> getGuilds() {
		return guilds;
	}

	public static Guild getGuild(IGuild guild) {
		if (!guilds.contains(guild))
			guilds.add(new Guild(guild));
		return guilds.get(guilds.indexOf(guild));
	}

	@EventSubscriber
	public void onReady(ReadyEvent e) {
		mongo.loadUsers();
		buildCommands();
	}
}
