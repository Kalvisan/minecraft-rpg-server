package com.github.kalvisan.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.kalvisan.objects.Classes;

public final class HeroClass {
	
	private Player player;
	private Classes hero;
	
	private double exp;
	private int level;
	private List<String> waypoints = new ArrayList<String>();
	private Inventory backpack;
	private Inventory bank;

	public HeroClass(Player player, Classes hero) {
		this.player = player;
		this.hero = hero;
		
		// Default variables
		backpack = player.getServer().createInventory(player, 9 * 6, "Backpack");
		bank = player.getServer().createInventory(player, 9 * 6, "Bank");
		this.exp = 0;
		this.level = 1;
		this.waypoints.add("1");
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Classes getHero() {
		return hero;
	}

	public void setHero(Classes hero) {
		this.hero = hero;
	}
	

	public double getExp() {
		return exp;
	}

	public void setExp(double exp) {
		this.exp = exp;
	}

	public void addWaypoint(String waypointID) {
		this.waypoints.add(waypointID);
	}
	
	public List<String> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<String> list) {
		this.waypoints = list;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Inventory getBackpack() {
		return this.backpack;
	}

	public void setBackpack(Inventory backpack) {
		this.backpack = backpack;
	}

	public Inventory getBank() {
		return bank;
	}

	public void setBank(Inventory bank) {
		this.bank = bank;
	}
	
}
