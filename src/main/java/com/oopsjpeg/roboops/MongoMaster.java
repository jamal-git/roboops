package com.oopsjpeg.roboops;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.oopsjpeg.roboops.storage.GuildWrapper;
import com.oopsjpeg.roboops.storage.UserWrapper;
import org.bson.Document;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MongoMaster extends MongoClient {
	private final MongoDatabase database = getDatabase("roboops");
	private final MongoCollection<Document> users = database.getCollection("users");
	private final MongoCollection<Document> guilds = database.getCollection("guilds");

	public void loadUsers() {
		Roboops.LOGGER.info("Loading users...");
		for (Document d : users.find()) {
			UserWrapper u = inUser(d);
			if (u != null) Roboops.getUsers().add(inUser(d));
		}
		Roboops.LOGGER.info("Loaded users.");
	}

	public void loadUser(IUser u) {
		Document d = users.find(Filters.eq(u.getLongID())).first();
		Roboops.getUsers().remove(u);
		Roboops.getUsers().add(inUser(d));
	}

	public void loadGuilds() {
		Roboops.LOGGER.info("Loading guilds...");
		for (Document d : guilds.find()) {
			GuildWrapper g = inGuild(d);
			if (g != null) Roboops.getGuilds().add(inGuild(d));
		}
		Roboops.LOGGER.info("Loaded guilds.");
	}

	public void loadGuild(IGuild g) {
		Document d = guilds.find(Filters.eq(g.getLongID())).first();
		Roboops.getGuilds().remove(g);
		Roboops.getGuilds().add(inGuild(d));
	}

	public void saveUsers() {
		Roboops.LOGGER.info("Saving users...");
		for (UserWrapper u : Roboops.getUsers()) saveUser(u);
		Roboops.LOGGER.info("Saved users.");
	}

	public void saveUser(UserWrapper u) {
		users.replaceOne(Filters.eq(u.getID()), outUser(u), new ReplaceOptions().upsert(true));
	}

	public void saveGuilds() {
		Roboops.LOGGER.info("Saving guilds...");
		for (GuildWrapper g : Roboops.getGuilds()) saveGuild(g);
		Roboops.LOGGER.info("Saved guilds.");
	}

	public void saveGuild(GuildWrapper g) {
		guilds.replaceOne(Filters.eq(g.getID()), outGuild(g), new ReplaceOptions().upsert(true));
	}

	public UserWrapper inUser(Document d) {
		UserWrapper u = new UserWrapper(Roboops.getClient().getUserByID(d.getLong("_id")));
		if (d.containsKey("desc"))
			u.setDesc(d.get("desc", ""));
		if (d.containsKey("money"))
			u.setMoney(d.get("money", 0));
		if (d.containsKey("most_money"))
			u.setMostMoney(d.get("most_money", 0));
		if (d.containsKey("last_daily"))
			u.setLastDaily(LocalDateTime.parse(d.get("last_daily", "")));
		return u;
	}

	public Document outUser(UserWrapper u) {
		Document d = new Document("_id", u.getID());
		if (u.getDesc() != null)
			d.append("desc", u.getDesc());
		d.append("money", u.getMoney());
		d.append("most_money", u.getMostMoney());
		if (u.getLastDaily() != null)
			d.append("last_daily", u.getLastDaily().toString());
		return d;
	}

	public GuildWrapper inGuild(Document d) {
		GuildWrapper g = new GuildWrapper(Roboops.getClient().getGuildByID(d.getLong("_id")));
		if (d.containsKey("self_roles"))
			g.setSelfRoles(((ArrayList<Long>) d.get("self_roles")).stream()
					.map(l -> Roboops.getClient().getRoleByID(l))
					.collect(Collectors.toList()));
		return g;
	}

	public Document outGuild(GuildWrapper g) {
		Document d = new Document("_id", g.getID());
		d.append("self_roles", g.getSelfRoles().stream()
				.map(IRole::getLongID)
				.collect(Collectors.toList()));
		return d;
	}

}
