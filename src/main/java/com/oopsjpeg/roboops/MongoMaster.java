package com.oopsjpeg.roboops;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.oopsjpeg.roboops.storage.Guild;
import com.oopsjpeg.roboops.storage.User;
import org.bson.Document;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MongoMaster extends MongoClient {
	private final MongoDatabase database = getDatabase("roboops");
	private final MongoCollection<Document> users = database.getCollection("users");
	private final MongoCollection<Document> guilds = database.getCollection("guilds");

	public void loadUsers() {
		Roboops.LOGGER.info("Loading user documents...");
		for (Document d : users.find()) {
			User u = inUser(d);
			if (u != null) Roboops.getUsers().add(inUser(d));
		}
		Roboops.LOGGER.info("Successfully loaded users.");
	}

	public void loadUser(IUser u) {
		Document d = (Document) Filters.eq(u.getLongID());
		Roboops.getUsers().add(inUser(d));
	}

	public void loadUser(User u) {
		loadUser(u.getUser());
	}

	public void loadGuilds() {
		Roboops.LOGGER.info("Loading guild documents...");
		for (Document d : guilds.find()) {
			Guild g = inGuild(d);
			if (g != null) Roboops.getGuilds().add(inGuild(d));
		}
		Roboops.LOGGER.info("Successfully loaded guilds.");
	}

	public void loadGuild(IGuild g) {
		Document d = (Document) Filters.eq(g.getLongID());
		Roboops.getGuilds().add(inGuild(d));
	}

	public void loadGuild(Guild g) {
		loadGuild(g.getGuild());
	}

	public void saveUsers() {
		Roboops.LOGGER.info("Saving user documents...");
		for (User u : Roboops.getUsers()) saveUser(u);
		Roboops.LOGGER.info("Successfully saved users.");
	}

	public void saveUser(User u) {
		users.replaceOne(Filters.eq(u.getID()), outUser(u), new ReplaceOptions().upsert(true));
	}

	public void saveUser(IUser u) {
		saveUser(Roboops.getUser(u));
	}

	public void saveGuilds() {
		Roboops.LOGGER.info("Saving guild documents...");
		for (Guild g : Roboops.getGuilds()) saveGuild(g);
		Roboops.LOGGER.info("Successfully saved guilds.");
	}

	public void saveGuild(Guild g) {
		guilds.replaceOne(Filters.eq(g.getID()), outGuild(g), new ReplaceOptions().upsert(true));
	}

	public void saveGuild(IGuild g) {
		saveGuild(Roboops.getGuild(g));
	}

	public User inUser(Document d) {
		User u = new User(Roboops.getClient().getUserByID(d.getLong("_id")));
		if (d.containsKey("desc"))
			u.setDesc((String) d.getOrDefault("desc", ""));
		if (d.containsKey("money"))
			u.setMoney((int) d.getOrDefault("money", 0));
		if (d.containsKey("most_money"))
			u.setMostMoney((int) d.getOrDefault("most_money", 0));
		if (d.containsKey("last_daily"))
			u.setLastDaily(LocalDateTime.parse((String) d.getOrDefault("last_daily", "")));
		return u;
	}

	public Document outUser(User u) {
		Document d = new Document("_id", u.getID());
		if (u.getDesc() != null)
			d.append("desc", u.getDesc());
		d.append("money", u.getMoney());
		d.append("most_money", u.getMostMoney());
		if (u.getLastDaily() != null)
			d.append("last_daily", u.getLastDaily().toString());
		return d;
	}

	public Guild inGuild(Document d) {
		Guild g = new Guild(Roboops.getClient().getGuildByID(d.getLong("_id")));
		if (d.containsKey("blacklist_channels")) {
			long[] ids = (long[]) d.getOrDefault("blacklist_channels", new long[0]);
			if (ids.length > 0)
				g.setBlacklistChannels(StreamSupport.stream(Arrays.spliterator(ids), false)
						.map(i -> Roboops.getClient().getChannelByID(i))
						.collect(Collectors.toList()));
		}
		return g;
	}

	public Document outGuild(Guild g) {
		Document d = new Document("_id", g.getID());
		d.append("blacklist_channels", g.getBlacklistChannels().stream()
				.map(IChannel::getLongID)
				.toArray(Long[]::new));
		return d;
	}

}
