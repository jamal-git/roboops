package com.oopsjpeg.roboops;

import com.mongodb.MongoException;
import com.oopsjpeg.roboops.commands.*;
import com.oopsjpeg.roboops.commands.sars.*;
import com.oopsjpeg.roboops.framework.commands.CommandCenter;
import com.oopsjpeg.roboops.storage.GuildWrapper;
import com.oopsjpeg.roboops.storage.UserWrapper;
import com.oopsjpeg.roboops.util.Util;
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
import java.util.stream.Collectors;

public class Roboops {
	public static final Logger LOGGER = LoggerFactory.getLogger(Roboops.class.getName());
	public static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(2);

	private static final CommandCenter commands = new CommandCenter();
	private static final List<UserWrapper> users = new ArrayList<>();
	private static final List<GuildWrapper> guilds = new ArrayList<>();

	private static MongoMaster mongo;
	private static IDiscordClient client;
	private static String token;
	private static String prefix;

	public static void main(String[] args) {
		if (loadConfig() && openMongo()) {
			commands.setPrefix(prefix);
			client = new ClientBuilder().withToken(token).build();
			client.getDispatcher().registerListener(new Roboops());
			client.getDispatcher().registerListener(commands);
			client.login();
		}
	}

	private static File getConfigFile() {
		return new File("config.ini");
	}

	private static boolean loadConfig() {
		LOGGER.info("Loading config...");

		// Create config if doesn't exist
		if (!getConfigFile().exists()) createConfig();
			// False if unable to read config
		else if (!getConfigFile().canRead())
			LOGGER.error("Unable to read config.");
		else {
			try {
				// Open reader
				FileReader fr = new FileReader(getConfigFile());

				// Load the config
				Properties initProp = new Properties();
				initProp.load(fr);
				// Close the reader
				fr.close();
				// Create copy of config to store new fields
				Properties newProp = new Properties();
				newProp.putAll(initProp);
				// Load fields from config
				token = Util.getProperty(initProp, newProp, "token");
				prefix = Util.getProperty(initProp, newProp, "prefix");

				// Store copy if necessary
				if (!initProp.equals(newProp)) {
					FileWriter fw = new FileWriter(getConfigFile());
					newProp.store(fw, "roboops config");
					fw.close();
				}

				// Close reader
				fr.close();
				LOGGER.info("Loaded config.");

				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	private static void createConfig() {
		LOGGER.info("Creating config...");

		try {
			// False if unable to write file
			if (getConfigFile().exists() && !getConfigFile().canWrite())
				LOGGER.error("Unable to write config.");
			else {
				// Open writer
				FileWriter fw = new FileWriter(getConfigFile());

				// Set properties to defaults
				Properties prop = new Properties();
				prop.putAll(Util.getDefaultConfig());
				// Write the config
				prop.store(fw, "roboops config");

				// Close writer
				fw.close();
				LOGGER.info("Created config, please setup 'config.ini'.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean openMongo() {
		LOGGER.info("Opening mongo connection...");
		try {
			// Connect to the mongo server
			mongo = new MongoMaster();
			// Disconnect the mongo client when shutting down
			Runtime.getRuntime().addShutdownHook(new Thread(() -> mongo.close()));

			LOGGER.info("Opened mongo connection.");
			return true;
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void buildCommands() {
		commands.clear();

		commands.add(new BalanceCommand());
		commands.add(new CleanCommand());
		commands.add(new DailyCommand());
		commands.add(new DescCommand());
		commands.add(new GambleCommand());
		commands.add(new LeaderboardCommand());
		commands.add(new PayCommand());
		commands.add(new ProfileCommand());

		commands.add(new AddSARCommand());
		commands.add(new ClearSARCommand());
		commands.add(new ListSARCommand());
		commands.add(new RemoveSARCommand());
		commands.add(new RoleCommand());
	}

	public static CommandCenter getCommands() {
		return commands;
	}

	public static List<UserWrapper> getUsers() {
		return users;
	}

	public static UserWrapper getUser(IUser user) {
		if (!users.contains(user)) users.add(new UserWrapper(user));
		return users.get(users.indexOf(user));
	}

	public static List<GuildWrapper> getGuilds() {
		return guilds;
	}

	public static GuildWrapper getGuild(IGuild guild) {
		if (!guilds.contains(guild))
			guilds.add(new GuildWrapper(guild));
		return guilds.get(guilds.indexOf(guild));
	}

	public static MongoMaster getMongo() {
		return mongo;
	}

	public static IDiscordClient getClient() {
		return client;
	}

	public static String getPrefix() {
		return prefix;
	}

	@EventSubscriber
	public void onReady(ReadyEvent event) {
		buildCommands();
		mongo.loadUsers();
		mongo.loadGuilds();

		System.out.println("https://discordapp.com/oauth2/authorize?client_id="
				+ client.getApplicationClientID() + "&scope=bot");
		System.out.println("Guilds: " + client.getGuilds().stream()
				.map(IGuild::getName).collect(Collectors.joining(", ")));
	}
}
