package com.oopsjpeg.roboops.storage;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IUser;

import java.time.LocalDateTime;

public class UserWrapper {
	private final long id;
	private String desc;

	// Currency
	private int money = 0;
	private int mostMoney = 0;
	private LocalDateTime lastDaily;

	// Temp Variables
	private boolean gambling;

	public UserWrapper(IUser user) {
		this.id = user.getLongID();
	}

	public long getID() {
		return id;
	}

	public IUser getUser() {
		return Roboops.getClient().getUserByID(id);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = Math.max(0, money);
		if (this.money > mostMoney)
			mostMoney = this.money;
	}

	public void giveMoney(int money) {
		setMoney(this.money + money);
	}

	public void takeMoney(int money) {
		setMoney(this.money - money);
	}

	public int getMostMoney() {
		return mostMoney;
	}

	public void setMostMoney(int mostMoney) {
		this.mostMoney = mostMoney;
	}

	public LocalDateTime getLastDaily() {
		return lastDaily;
	}

	public void setLastDaily(LocalDateTime lastDaily) {
		this.lastDaily = lastDaily;
	}

	public boolean canDaily() {
		return lastDaily == null || LocalDateTime.now().isAfter(lastDaily.plusDays(1));
	}

	public int daily() {
		lastDaily = LocalDateTime.now();
		return Util.randInt(200, 300);
	}

	public String dailyRemaining() {
		if (lastDaily == null) return null;
		return Util.timeDiff(LocalDateTime.now(), lastDaily.plusDays(1));
	}

	public boolean isGambling() {
		return gambling;
	}

	public void setGambling(boolean gambling) {
		this.gambling = gambling;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& ((obj instanceof UserWrapper && ((UserWrapper) obj).id == id)
				|| (obj instanceof IUser) && id == ((IUser) obj).getLongID());
	}
}
