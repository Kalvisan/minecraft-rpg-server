package com.github.kalvisan.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.github.kalvisan.MainClassRPG;
import com.github.kalvisan.api.ParticleEffect;
import com.github.kalvisan.api.Title;
import com.github.kalvisan.classes.HeroClass;
import com.github.kalvisan.objects.WaypointClass;

public class WaypointGUI implements Listener {
	private MainClassRPG plugin;
	static int invSize = 9 * 2;
	static ItemStack open = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
	static ItemStack close = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
	public static ArrayList<WaypointClass> waypoints = null;

	public static Inventory gui = Bukkit.createInventory(null, invSize, "Waypoint");
	public static HashMap<Player, Location> clickedWaypoint = new HashMap<>();

	public WaypointGUI(MainClassRPG p) {
		this.plugin = p;
		waypoints = new ArrayList<WaypointClass>();
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		saveHashmap();
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		waypoints.clear();
		loadHashmap();
	}

	public static void openGUI(Player p, Location blockLoc) {
		if (p.hasPermission("cubeplex.player.warpgui") && MainClassRPG.classes.containsKey(p.getUniqueId())) {
			HeroClass playerHero = MainClassRPG.classes.get(p.getUniqueId());
			int i = 0;

			for (WaypointClass wp : waypoints) {
				ItemMeta c = close.getItemMeta();
				c.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + wp.getWarpName());
				close.setItemMeta(c);
				
				// TODO: fix wrong open/close waypoints
				
				if (wp.getWarpLocation().equals(blockLoc)) {
					if(!playerHero.getWaypoints().contains(wp.getId())) {
						playerHero.addWaypoint(wp.getId());
					}
				}
				for (String id : playerHero.getWaypoints()) {
					if (wp.getId().equalsIgnoreCase(id)) {
						ItemMeta im = open.getItemMeta();
						im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + wp.getWarpName());
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("§6Coordinates:");
						lore.add("§8X: §7" + wp.getWarpEndLocation().getX());
						lore.add("§8Y: §7" + wp.getWarpEndLocation().getY());
						lore.add("§8Z: §7" + wp.getWarpEndLocation().getZ());
						lore.add("§7World: §7" + wp.getWarpEndLocation().getWorld().getName());
						lore.add(" ");
						lore.add("§e§lClick to use waypoint");
						im.setLore(lore);
						open.setItemMeta(im);

						gui.setItem(i, open);
					} else {
						gui.setItem(i, close);
					}
				}
				i++;
			}

			clickedWaypoint.put(p, blockLoc);
			p.openInventory(gui);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (e.getInventory().getName().equalsIgnoreCase(gui.getName()) && c != null) {
			Location loc = clickedWaypoint.get(p);
			clickedWaypoint.remove(p);
			
			if (!c.hasItemMeta() || !c.equals(open)) {
				e.setCancelled(true);
				p.closeInventory();
				return;
			}

			for (WaypointClass wp : waypoints) {
				if (ChatColor.stripColor(c.getItemMeta().getDisplayName()).equalsIgnoreCase(wp.getWarpName())) {
					e.setCancelled(true);
					p.closeInventory();
					
					new BukkitRunnable() {
						int t = 4;
						double x = loc.getX() + 0.5D;
						double y = loc.getY() + 1.0D;
						double z = loc.getZ() + 0.5D;
						World world = loc.getWorld();

						@Override
						public void run() {
							this.t -= 1;
							Title.sendTitle(p, "&a&l" + this.t, 1, 1, 1);
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10 + this.t, 1);
							if (this.t < 1) {
								p.closeInventory();
								p.teleport(wp.getWarpEndLocation());
								ParticleEffect.CLOUD.display(0.5F, 1F, 0.5F, 0.01F, 15, p.getLocation().add(0, 1, 0), 50D);
								Title.sendTitle(p, "&6&l" + wp.getWarpName(), 1, 4, 2);
								cancel();
								return;
							} else if (this.t == 1) {
								p.setVelocity(new Vector(0, 8, 0));
							} else if (this.t > 1) {
								p.teleport(new Location(world, x, y, z));
							}
						}
					}.runTaskTimer(plugin, 20L, 20L);
					return;
				}
			}

			e.setCancelled(true);
			p.closeInventory();
		}
	}

	@EventHandler
	public void onWaypointClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block i = event.getClickedBlock();
			if (i.getType().equals(Material.ENDER_PORTAL_FRAME)) {
				WaypointGUI.openGUI(event.getPlayer(), event.getClickedBlock().getLocation());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerSettingWaypoint(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getPlayer().getItemInHand().getType().equals(Material.ENDER_PORTAL_FRAME))) {
			if (event.getPlayer().getItemInHand().hasItemMeta())
				if ((event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Waypoint"))) {
					Player player = event.getPlayer();
					ItemStack is = player.getItemInHand();
					String id = ChatColor.stripColor(is.getItemMeta().getLore().get(0));
					String name = ChatColor.stripColor(is.getItemMeta().getLore().get(1));
					int radius = 5;
					for (int x = -(radius); x <= radius; x++) {
						for (int y = -(radius); y <= radius; y++) {
							for (int z = -(radius); z <= radius; z++) {
								Block block = (new Location(player.getWorld(), player.getLocation().getX() + x, player.getLocation().getY() + y, player.getLocation().getZ() + z)).getBlock();
								if (block.getType().equals(Material.ENDER_PORTAL_FRAME)) {
									waypoints.add(new WaypointClass(id, name, block.getLocation(), event.getClickedBlock().getLocation().add(0.5, 3, 0.5)));
									player.sendMessage(MainClassRPG.prefix + "Waypoint added! :)");
									player.setItemInHand(new ItemStack(Material.AIR));
									return;
								}
							}
						}
					}
					player.sendMessage(MainClassRPG.prefix + "Can't find: " + Material.ENDER_PORTAL_FRAME.toString());
				}
		}
	}

	///////////////////////////////////////////////////////////////////////

	public void saveHashmap() {
		String pluginFolder = this.plugin.getDataFolder().getAbsolutePath();
		File file = new File(pluginFolder + File.separator + "waypoints.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
				MainClassRPG.log("[WayPoints] creating new file '" + file.getAbsolutePath() + "'.", Level.INFO);
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (WaypointClass l : WaypointGUI.waypoints) {

				bw.write(l.getId() + "#" + l.getWarpName() + "#" + l.getWarpEndLocation().getWorld().getName() + "#" + l.getWarpLocation().getX() + "@" + l.getWarpLocation().getY() + "@" + l.getWarpLocation().getZ() + "#" + l.getWarpEndLocation().getX() + "@" + l.getWarpEndLocation().getY() + "@" + l.getWarpEndLocation().getZ());

				bw.newLine();
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			MainClassRPG.log("[WayPoints] File '" + file.getAbsolutePath() + "' not found.", Level.WARNING);
		}
	}

	public void loadHashmap() {
		String pluginFolder = this.plugin.getDataFolder().getAbsolutePath();
		File file = new File(pluginFolder + File.separator + "waypoints.txt");
		if (file.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line = "";
				while ((line = br.readLine()) != null) {
					String[] locs = line.split("#");

					String id = locs[0];
					String warpName = locs[1];
					String world = locs[2];

					String[] waypoint = locs[3].split("@");
					double x = Float.valueOf(waypoint[0]);
					double y = Float.valueOf(waypoint[1]);
					double z = Float.valueOf(waypoint[2]);

					String[] endWaypoint = locs[4].split("@");
					double x2 = Float.valueOf(endWaypoint[0]);
					double y2 = Float.valueOf(endWaypoint[1]);
					double z2 = Float.valueOf(endWaypoint[2]);

					WaypointGUI.waypoints.add(new WaypointClass(id, warpName, new Location(plugin.getServer().getWorld(world), x, y, z), new Location(plugin.getServer().getWorld(world), x2, y2, z2)));
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
