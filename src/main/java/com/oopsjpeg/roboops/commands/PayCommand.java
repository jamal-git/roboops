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

import java.util.Arrays;

public class PayCommand implements Command {
	@Override
	public void execute(IMessage message, String alias, String[] args) {
		IUser author = message.getAuthor();
		IChannel channel = message.getChannel();
		UserWrapper info = Roboops.getUser(author);

		if (info.getMoney() <= 0)
			Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
					+ "you do not have any money.");
		else if (args.length < 2)
			Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
					+ "the correct syntax is: `" + Roboops.getPrefix() + "pay <amount> <user>`");
		else {
			try {
				int amount = Integer.parseInt(args[0]);
				if (amount <= 0)
					Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
							+ "the pay must be higher than 0.");
				else if (info.getMoney() < amount)
					Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
							+ "you do not have **$" + Util.comma(amount) + "**.");
				else {
					IUser target = Util.findUser(message.getClient(), Arrays.copyOfRange(args, 1, args.length));
					if (target == null)
						Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
								+ "I couldn't find that user.");
					else {
						Bufferer.deleteMessage(message);
						UserWrapper targetInfo = Roboops.getUser(target);
						info.subMoney(amount);
						targetInfo.addMoney(amount);
						Roboops.getMongo().saveUser(info);
						Roboops.getMongo().saveUser(targetInfo);
						Bufferer.sendMessage(channel, RoboopsEmote.SUCCESS + "**" + author.getName() + "** "
								+ "donated **" + target.getName() + " to **$" + amount + "**.");
					}
				}
			} catch (NumberFormatException e) {
				Bufferer.sendMessage(channel, RoboopsEmote.ERROR + "**" + author.getName() + "**, "
						+ "the correct syntax is: `" + Roboops.getPrefix() + "pay <amount> <user>`");
			}
		}
	}

	@Override
	public String getName() {
		return "pay";
	}

	@Override
	public String getDesc() {
		return "Pay another user a specified amount of money.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"donate"};
	}
}
