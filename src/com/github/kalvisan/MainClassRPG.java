package com.github.kalvisan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.kalvisan.api.ItemframeProtect;
import com.github.kalvisan.classes.HeroClass;
import com.github.kalvisan.classes.InventoryControl;
import com.github.kalvisan.classes.sorceress.Wand;
import com.github.kalvisan.commands.CommandRole;
import com.github.kalvisan.extra.Chair;
import com.github.kalvisan.extra.ExtraBlocks;
import com.github.kalvisan.extra.Timer;
import com.github.kalvisan.gui.SelectClass;
import com.github.kalvisan.gui.ServerSelect;
import com.github.kalvisan.gui.ShopGUI;
import com.github.kalvisan.gui.SneakGUI;
import com.github.kalvisan.gui.WaypointGUI;
import com.github.kalvisan.listeners.CheckItemLore;
import com.github.kalvisan.listeners.GlobalListener;
import com.github.kalvisan.mysql.DBManager;
import com.github.kalvisan.objects.RespawnBoxClass;

public class MainClassRPG extends JavaPlugin implements Listener {

	private static String pluginName = "CubePlexRPG";
	public static MainClassRPG instance;
	public static DBManager mysql;
	public int task;

	public static String prefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "" + ChatColor.BOLD + "Info" + ChatColor.GRAY + "] " + ChatColor.YELLOW;
	public static HashMap<UUID, HeroClass> classes = new HashMap<>();

	public static MainClassRPG getInstance() {
		return (MainClassRPG) Bukkit.getPluginManager().getPlugin(pluginName);
	}

	public static String pluginPath() {
		return Bukkit.getPluginManager().getPlugin(pluginName).getDataFolder().getAbsolutePath();
	}

	public void onEnable() {
		instance = this;
		task = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Timer(this), 20L, 20L);
		registerListeners();

		PluginDescriptionFile pdfFile = getDescription();
		MainClassRPG.log("Has been ENABLED! v" + pdfFile.getVersion(), Level.INFO);
		
		for(Player player : getServer().getOnlinePlayers()) {
			player.teleport(getServer().getWorld(getConfig().getString("lobby_world")).getSpawnLocation());
		}
	}

	public void registerListeners() {
		ConfigGenerator cg = new ConfigGenerator(this);
		cg.CrateDefaultConfig();

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventoryControl(this), this);
		pm.registerEvents(new ItemframeProtect(this), this);
		pm.registerEvents(new CheckItemLore(this), this);
		pm.registerEvents(new GlobalListener(this), this);
		pm.registerEvents(new ExtraBlocks(this), this);
		pm.registerEvents(new Chair(this), this);
		pm.registerEvents(new Wand(this), this);
		
		pm.registerEvents(new ServerSelect(this), this);
		pm.registerEvents(new SelectClass(this), this);
		pm.registerEvents(new WaypointGUI(this), this);
		pm.registerEvents(new ShopGUI(this), this);
		pm.registerEvents(new SneakGUI(), this);
		
		getCommand("role").setExecutor(new CommandRole());
		mysql = new DBManager(this);
		//mysql.startDB();
	}

	public void onDisable() {
		MainClassRPG.log("Has been DISABLED! v" + getDescription().getVersion(), Level.INFO);
		//mysql.closeDB();
		for(Player p : getServer().getOnlinePlayers()) {
			savePlayerData(p);	
		}
		
		for (Entry<RespawnBoxClass, Integer> b : Timer.boxOnTimer.entrySet()) {
			Block block = b.getKey().getBlock();
			block.setType(b.getKey().getMaterial());
			Timer.boxOnTimer.remove(b.getKey());
		}
		instance = null;
	}

	public static void log(String s, Level l) {
		getInstance().getLogger().log(l, ChatColor.AQUA + "[-[" + pluginName + "]-] " + s);
	}
	
	public static void savePlayerData(Player player) {
		if(!classes.containsKey(player.getUniqueId())) {
			return;
		}
		
		File file = new File(MainClassRPG.pluginPath() + File.separator + "data", player.getUniqueId().toString() + ".yml");
		
		if (file.exists()) {
			HeroClass hc = MainClassRPG.classes.get(player.getUniqueId());
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			CommandRole.saveConfigFile(config, player, hc);
			
			try {
				config.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		player.setLevel(0);
		player.setExp(0);
		player.getInventory().clear();
		classes.remove(player.getUniqueId());
		
	}

	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + "You'r not player to execute a command!");
			return false;
		}
		final Player player = (Player) s;

		if (l.equalsIgnoreCase("ticket")) {
			player.sendMessage("");
			player.sendMessage(ChatColor.YELLOW + "=============================================");
			player.sendMessage(ChatColor.GREEN + "If you spot a bug or just have a suggestion, please, leave us to know about it!");
			player.sendMessage("");
			player.sendMessage(ChatColor.GOLD + "You can report your ticket here: " + ChatColor.RED + "http://www.cubeplex.eu/support/");
			return true;
		}

		if (l.equalsIgnoreCase("cc")) {
			if ((player.hasPermission("mod.cc")) || (player.isOp())) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					for (int i = 1; i < 55; i++) {
						p.sendMessage(" ");
					}
					if (p.hasPermission("mod.cc")) {
						p.sendMessage(ChatColor.RED + "Player [" + player.getName() + "] use clean command!");
					}
				}
				player.sendMessage(ChatColor.GOLD + "Clean...");
			}
			return true;
		}

		if (l.equalsIgnoreCase("servers")) {
			player.openInventory(ServerSelect.gui);
			return true;
		}

		if (l.equalsIgnoreCase("shop")) {
			player.openInventory(ShopGUI.gui);
			return true;
		}
		
		if (l.equalsIgnoreCase("cubeplex") && player.hasPermission("cubeplex.admin.command")) {
			if (a[0].equalsIgnoreCase("target")) {
//				HashSet<Material> transparent = new HashSet<Material>();
//				transparent.add(Material.AIR);
//				
//				TargetDummy target = new TargetDummy(this);
//				target.addTarget(player.getTargetBlock(transparent, 50).getLocation());
				ItemStack is = new ItemStack(Material.LEATHER, 1);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("Target");
				is.setItemMeta(im);
				player.getInventory().addItem(is);
				player.updateInventory();
				player.sendMessage(prefix + "Target adder added!!");
				return true;
			}
			if (a[0].equalsIgnoreCase("waypoint")) {
				if(a.length < 2) {
					player.sendMessage(prefix + "/cubeplex waypoint <id> <warp_name>");
					return true;
				}
				
				if(a[1].equalsIgnoreCase("remove")) {
					if(a.length < 2) {
						player.sendMessage(prefix + "/cubeplex remove <id>");
						return true;
					}
					WaypointGUI.waypoints.remove(a[2]);
					player.sendMessage(prefix + "Waypoint removed!");
					return true;
				}
				
				ItemStack is = new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("Waypoint");
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(a[1]);
				lore.add(a[2]);
				im.setLore(lore);
				is.setItemMeta(im);
				
				player.getInventory().addItem(is);
				player.updateInventory();
				player.sendMessage(prefix + "Waypoint adder added!!");
				return true;
			}
			if (a[0].equalsIgnoreCase("world")) {
				if(a.length < 1) {
					player.sendMessage(prefix + "/cubeplex world <world name>");
					return true;
				}
				boolean exists = false;
				for(World w : player.getServer().getWorlds()) {
					if(w.getName().equalsIgnoreCase(a[1])) {
						exists = true;
						player.teleport(w.getSpawnLocation());
					}
				}
				
				if(exists) {
					player.sendMessage(prefix + "Teleported to world: " + a[1]);	
				} else {
					player.sendMessage(prefix + "World don't exist: " + a[1]);
				}
				return true;
			}
			return true;
		}
		
		if (l.equalsIgnoreCase("vote")) {
			player.sendMessage("");
			player.sendMessage(ChatColor.YELLOW + "=============================================");
			player.sendMessage(ChatColor.GREEN + "For each site you vote you get reward: " + ChatColor.GOLD + "" + ChatColor.BOLD + "1 Voting Key");
			player.sendMessage("");
			player.sendMessage(ChatColor.GOLD + "You can vote here: " + ChatColor.RED + "http://www.cubeplex.eu/vote/");
			return true;
		}

		if (l.equalsIgnoreCase("lore") && player.hasPermission("cubeplex.mod.lore")) {
			if (a.length <= 1) {
				player.sendMessage(prefix + "Use /lore name <names...>");
				player.sendMessage(prefix + "Use /lore lore <lores...>");
				player.sendMessage(prefix + "To set lore use SPACE to change line and to add space between words use '_' ");
				return true;
			}
			if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
				player.sendMessage(prefix + "Item unknown!");
				return true;
			}

			if (a[0].equalsIgnoreCase("name")) {
				String name = "";
				for (int i = 1; i <= a.length - 1; i++) {
					name += a[i].replace("&", "§") + " ";
				}
				ItemStack item = player.getItemInHand();
				ItemMeta itemmeta = item.getItemMeta();
				itemmeta.setDisplayName(name.trim());
				item.setItemMeta(itemmeta);
				player.sendMessage(prefix + "Name changed to: " + name.trim());
				return true;
			}

			if (a[0].equalsIgnoreCase("lore")) {
				ArrayList<String> lore = new ArrayList<String>();
				for (int i = 1; i <= a.length - 1; i++) {
					lore.add(a[i].replace("&", "§").replace("_", " "));
				}

				ItemStack item = player.getItemInHand();
				ItemMeta itemmeta = item.getItemMeta();
				itemmeta.setLore(lore);
				item.setItemMeta(itemmeta);
				player.sendMessage(prefix + "Item lore changed!");
				return true;
			}

			player.sendMessage(prefix + "Use /lore <name,lore> <lore...>");
			return true;
		}

		return false;
	}
}
