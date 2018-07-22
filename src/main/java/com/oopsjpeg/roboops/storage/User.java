package com.oopsjpeg.roboops.storage;

import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IUser;

import java.time.LocalDateTime;

public class User {
	private final IUser user;
	private String desc;

	// Currency
	private int money = 0;
	private int mostMoney = 0;
	private LocalDateTime lastDaily;

	// Temp Variables
	private boolean gambling;
	private IUser duelUser;

	public User(IUser user) {
		this.user = user;
	}

	public IUser getUser() {
		return user;
	}

	public String getName() {
		return user.getName();
	}

	public long getID() {
		return user.getLongID();
	}

	public String getAvatar() {
		return user.getAvatarURL();
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

	public void addMoney(int money) {
		setMoney(this.money + money);
	}

	public void subMoney(int money) {
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

	public int daily() {
		lastDaily = LocalDateTime.now();
		return Util.randInt(200, 300);
	}

	public boolean canDaily() {
		return lastDaily == null || LocalDateTime.now().isAfter(lastDaily.plusDays(1));
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

	public IUser getDuelUser() {
		return duelUser;
	}

	public void setDuelUser(IUser duelUser) {
		this.duelUser = duelUser;
	}

	@Override
	public boolean equals(Object obj) {
		return user.equals(obj);
	}

	@Override
	public String toString() {
		return user.toString();
	}
}
