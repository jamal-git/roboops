package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.UserWrapper;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;

public class PayCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		if (args.length < 2) return INVALID_USAGE;

		IUser author = message.getAuthor();
		IChannel channel = message.getChannel();
		UserWrapper info = Roboops.getUser(author);

		if (info.getMoney() <= 0)
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, you do not have any money.");
		else {
			try {
				int amount = Integer.parseInt(args[0]);
				if (amount <= 0)
					Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
							+ "the pay must be higher than 0.");
				else if (info.getMoney() < amount)
					Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
							+ "you do not have **$" + Util.comma(amount) + "**.");
				else {
					IGuild guild = message.getGuild();
					IUser target = Util.findUser(message.getClient(), Arrays.copyOfRange(args, 1, args.length));
					if (target == null || !guild.getUsers().contains(target))
						Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
								+ "I couldn't find that user.");
					else {
						UserWrapper targetInfo = Roboops.getUser(target);
						info.takeMoney(amount);
						targetInfo.giveMoney(amount);
						Roboops.getMongo().saveUser(info);
						Roboops.getMongo().saveUser(targetInfo);
						Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "** "
								+ "donated **$" + amount + "** to **" + target.getName() + "**.");
					}
				}
			} catch (NumberFormatException e) {
				return INVALID_USAGE;
			}
		}

		return SUCCESS;
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
	public String getUsage() {
		return "<amount> <user>";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"donate"};
	}
}
