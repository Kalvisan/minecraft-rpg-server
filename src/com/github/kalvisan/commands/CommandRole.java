package com.github.kalvisan.commands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.kalvisan.MainClassRPG;
import com.github.kalvisan.api.Title;
import com.github.kalvisan.classes.HeroClass;
import com.github.kalvisan.gui.SelectClass;
import com.github.kalvisan.objects.Classes;

public class CommandRole implements CommandExecutor {

	private void helpCommand(Player player) {
		player.sendMessage("help");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You are part of the undead?");
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			helpCommand(player);
			return true;
		}

		if (args[0].equalsIgnoreCase("class") && player.getWorld().getName().equalsIgnoreCase("world")) {
			if (args.length == 1) {
				SelectClass.openGUI(player);
				return true;
			}
			if (!MainClassRPG.classes.containsKey(player.getUniqueId()))
				for (Classes c : Classes.values()) {
					if (args[1].equalsIgnoreCase(c.getName())) {
						HeroClass hero = new HeroClass(player, c);

						File file = new File(MainClassRPG.pluginPath() + File.separator + "data" + File.separator + player.getUniqueId().toString() + ".yml");
						FileConfiguration customConfig = YamlConfiguration.loadConfiguration(file);
						if (!file.exists()) {
							try {
								//Player creates new file
								firstTimeCrateClass(player, hero.getHero());
								saveConfigFile(customConfig, player, hero);
								customConfig.save(file);
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							if(customConfig.contains(c.getName())) {
								
								// Player is old player
								restoreSaved(customConfig, player, hero);								
								
							} else {
								// Player create new Class, but not new file
								firstTimeCrateClass(player, hero.getHero());
								saveConfigFile(customConfig, player, hero);
							}
						}

						Title.sendFullTitle(player, "&6&l" + c.getName(), "&7" + c.getDescription(), 1, 4, 3);
						MainClassRPG.classes.put(player.getUniqueId(), hero);
					}
				}
			return true;
		}

		helpCommand(player);
		return true;
	}
	
	public static void firstTimeCrateClass(Player player, Classes c) {
		player.teleport(player.getServer().getWorld(MainClassRPG.getInstance().getConfig().getString("start_world")).getSpawnLocation());
		player.getInventory().setItem(0 ,new ItemStack(Material.BOOK, 1));
		player.getInventory().setItem(1 ,new ItemStack(Material.BOOK, 1));
		player.getInventory().setItem(2 ,new ItemStack(Material.BOOK, 1));
		ItemStack backpack = new ItemStack(Material.RABBIT_HIDE, 1);
		ItemMeta im = backpack.getItemMeta();
		im.setDisplayName("Backpack");
		backpack.setItemMeta(im);
		player.getInventory().setItem(8, backpack);
		player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "mm i give " + player.getName() + " " + c.getName() + " 1");
		player.setLevel(1);
		player.setExp(0);
	}

	public static void saveConfigFile(FileConfiguration config, Player player, HeroClass hero) {
		config.set("name", player.getName());
		config.set("ip", player.getAddress().getHostString());
		config.set("lastLogin", System.currentTimeMillis() / 1000L);
		config.set("bank", hero.getBank().getContents());

		List<String> waypoints = hero.getWaypoints();
		String waypointString = "1";
		if(waypoints != null) {
			waypointString = waypoints.get(0);
			for (int i = 1; i < waypoints.size(); i++) {
				waypointString += "#" + waypoints.get(i);
			}
		}
		config.set(hero.getHero().getName() + ".waypoints", waypointString);
		config.set(hero.getHero().getName() + ".level", hero.getLevel());
		config.set(hero.getHero().getName() + ".exp", hero.getExp());
		config.set(hero.getHero().getName() + ".last_world", player.getLocation().getWorld().getName());
		config.set(hero.getHero().getName() + ".inventory.content", player.getInventory().getContents());
		config.set(hero.getHero().getName() + ".inventory.backpack", hero.getBackpack().getContents());
	}
	
	public void restoreSaved(FileConfiguration config, Player player, HeroClass hero) {
		player.getInventory().clear();
		restoreInv(config, player, hero);
		String waypoints[] = config.getString(hero.getHero().getName() + ".waypoints").split("#");
		hero.setWaypoints(Arrays.asList(waypoints));
		hero.setExp(config.getInt(hero.getHero().getName() + ".exp"));
		player.setLevel(config.getInt(hero.getHero().getName() + ".level"));
		player.teleport(player.getServer().getWorld(config.getString(hero.getHero().getName() + ".last_world")).getSpawnLocation());
	}

	@SuppressWarnings("unchecked")
	public void restoreInv(FileConfiguration config, Player p, HeroClass hc) {
		ItemStack[] content = ((List<ItemStack>) config.get(hc.getHero().getName() + ".inventory.content")).toArray(new ItemStack[0]);
		p.getInventory().setContents(content);
		
		content = ((List<ItemStack>) config.get(hc.getHero().getName() + ".inventory.backpack")).toArray(new ItemStack[0]);
		hc.getBackpack().setContents(content);
		
		content = ((List<ItemStack>) config.get("bank")).toArray(new ItemStack[0]);
		hc.getBank().setContents(content);
	}

}
