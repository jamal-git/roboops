package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoboopsEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.UserWrapper;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;

public class GambleCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		UserWrapper info = Roboops.getUser(author);

		if (info.isGambling())
			Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
					+ "you are already gambling.");
		else if (args.length == 0)
			Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
					+ "the correct syntax is: `" + Roboops.getPrefix() + "gamble <amount>`");
		else {
			try {
				int amount = Integer.parseInt(args[0]);
				if (amount <= 0)
					Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
							+ "the gamble must be higher than 0.");
				else if (info.getMoney() < amount)
					Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
							+ "you do not have **$" + Util.comma(amount) + "**.");
				else {
					Bufferer.deleteMessage(message);
					info.setGambling(true);
					IMessage gamble = Bufferer.sendMessage(channel, RoboopsEmote.DICE + "**" + author.getName() + "** "
							+ "is gambling **$" + Util.comma(amount) + "**...!");
					Roboops.SCHEDULER.schedule(() -> {
						int roll = Util.randInt(100);
						if (roll == 0)
							Bufferer.editMessage(gamble, RoboopsEmote.DICE + "**" + author.getName() + "** "
									+ "rolled a(n) **" + roll + "** and kept their **$" + Util.comma(amount) + "**.");
						else {
							if (roll < 55) {
								info.subMoney(amount);
								Bufferer.editMessage(gamble, RoboopsEmote.DICE + "**" + author.getName() + "** "
										+ "rolled a(n) **" + roll + "** and lost their **$" + Util.comma(amount) + "**.");
							} else if (roll < 96) {
								info.addMoney(amount * 2);
								Bufferer.editMessage(gamble, RoboopsEmote.DICE + "**" + author.getName() + "** "
										+ "rolled a(n) **" + roll + "** and earned **x2** their **$" + Util.comma(amount) + "**, "
										+ "equaling **$" + Util.comma(amount * 2) + "**.");
							} else {
								info.addMoney(amount * 3);
								Bufferer.editMessage(gamble, RoboopsEmote.DICE + "**" + author.getName() + "** "
										+ "rolled a(n) **" + roll + "** and earned **x3** their **$" + Util.comma(amount) + "**, "
										+ "equaling **$" + Util.comma(amount * 3) + "**.");
							}
							Roboops.getMongo().saveUser(info);
						}
						info.setGambling(false);
					}, 3, TimeUnit.SECONDS);
				}
			} catch (NumberFormatException e) {
				Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
						+ "the correct syntax is: `" + Roboops.getPrefix() + "gamble <amount>`");
			}
		}
	}

	@Override
	public String getName() {
		return "gamble";
	}

	@Override
	public String getDesc() {
		return "Gamble your money.";
	}
}
