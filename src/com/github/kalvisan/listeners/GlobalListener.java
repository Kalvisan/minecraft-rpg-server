package com.github.kalvisan.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.github.kalvisan.MainClassRPG;
import com.github.kalvisan.api.Title;
import com.github.kalvisan.gui.SneakGUI;
import com.github.kalvisan.objects.Classes;

public class GlobalListener implements Listener {
	MainClassRPG plugin;

	public GlobalListener(MainClassRPG p) {
		this.plugin = p;
	}

	public static ArrayList<Material> diabledInteract = new ArrayList<>();

	static {
		diabledInteract.add(Material.ENDER_CHEST);
		diabledInteract.add(Material.TRAPPED_CHEST);
		diabledInteract.add(Material.IRON_TRAPDOOR);
		diabledInteract.add(Material.TRAP_DOOR);
		diabledInteract.add(Material.LEVER);
		diabledInteract.add(Material.FENCE_GATE);
		diabledInteract.add(Material.ACACIA_FENCE_GATE);
		diabledInteract.add(Material.BIRCH_FENCE_GATE);
		diabledInteract.add(Material.DARK_OAK_FENCE_GATE);
		diabledInteract.add(Material.JUNGLE_FENCE_GATE);
		diabledInteract.add(Material.SPRUCE_FENCE_GATE);
		diabledInteract.add(Material.WOODEN_DOOR);
		diabledInteract.add(Material.WOOD_DOOR);
		diabledInteract.add(Material.ACACIA_DOOR);
		diabledInteract.add(Material.BIRCH_DOOR);
		diabledInteract.add(Material.DARK_OAK_DOOR);
		diabledInteract.add(Material.JUNGLE_DOOR);
		diabledInteract.add(Material.SPRUCE_DOOR);		
		diabledInteract.add(Material.NOTE_BLOCK);
		diabledInteract.add(Material.REDSTONE_COMPARATOR);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		player.setLevel(1);
		player.teleport(plugin.getServer().getWorld(plugin.getConfig().getString("lobby_world")).getSpawnLocation());
		Title.sendFullTitle(player, plugin.getConfig().getString("firsttime.title"), plugin.getConfig().getString("firsttime.subtitle"), 2, 5, 3);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		p.teleport(p.getWorld().getSpawnLocation());
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		MainClassRPG.savePlayerData(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (MainClassRPG.classes.containsKey(p.getUniqueId())) {
			String format = e.getFormat();
			String lvl = String.valueOf(p.getLevel());

			format = "§7[§b" + MainClassRPG.classes.get(p.getUniqueId()).getHero().getName() + "§7 Lvl.§9" + lvl + "§7]§r " + format;
			e.setFormat(format);
		}
	}

	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(Material.SOIL))
			event.setCancelled(true);

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block i = event.getClickedBlock();
			Player p = event.getPlayer();
			if(!p.hasPermission("cubeplex.mod.use"))
			if (diabledInteract.contains(i.getType())) {
				event.setCancelled(true);
			}
			if (i.getType().equals(Material.ENDER_CHEST) && MainClassRPG.classes.containsKey(p.getUniqueId())) {
				p.openInventory(MainClassRPG.classes.get(p.getUniqueId()).getBank());
				p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 6, 1);
				event.setCancelled(true);
			}
			if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world"))
				if ((i.getState() instanceof Sign)) {
					BlockState stateBlock = i.getState();
					Sign sign = (Sign) stateBlock;
					Player player = event.getPlayer();

					if (!MainClassRPG.classes.containsKey(player.getUniqueId()))
						for (Classes c : Classes.values()) {
							if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[" + c.getName() + "]")) {
								player.chat("/role class " + c.getName());
							}
						}
				}
		}
	}

	//////////////////////// EXTRA EVENTS TO FIX BUGS ////////////////////

	@EventHandler
	public void onMobDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			if (damager.getVelocity().getY() < 0 && !damager.isOnGround() && !damager.getLocation().getBlock().getType().equals(Material.WATER) && !damager.getLocation().getBlock().getType().equals(Material.LADDER) && !damager.getLocation().getBlock().getType().equals(Material.VINE) && !damager.getLocation().getBlock().getRelative(0, 1, 0).getType().equals(Material.VINE) && !damager.hasPotionEffect(PotionEffectType.BLINDNESS) && damager.getVehicle() == null) {
				double origDamage = ((e.getDamage() - 1) / 150D) * 100D;
				e.setDamage(origDamage * 1.2);
			}
		}

		if ((e.getDamager() instanceof Projectile)) {
			Entity entity = (Entity) ((Projectile) e.getDamager()).getShooter();
			if ((entity instanceof Monster)) {
				if ((e.getEntity() instanceof Monster)) {
					e.setDamage(0);
					e.setCancelled(true);
				}
			}
		} else {
			if ((e.getEntity() instanceof Monster)) {
				if ((e.getDamager() instanceof Monster)) {
					e.setDamage(0);
					e.setCancelled(true);
				}
				if (e.getDamager() instanceof Player) {
					Player p = (Player) e.getDamager();
					Monster m = (Monster) e.getEntity();
					double health = m.getHealth() - e.getDamage();
					Title.sendActionBar(p, String.format("%.1f", e.getDamage()) + "&a&l damage on " + e.getEntity().getCustomName() + " &f- &c&l" + String.format("%.1f", (health < 0) ? 0 : health) + " / " + String.format("%.1f", m.getMaxHealth()) + "");
				}
			}
		}
	}

	// Opens SneakGUI then player sneaks
	@EventHandler
	public void onPlayerClickPlayer(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player) {
			if (event.getPlayer().isSneaking()) {
				SneakGUI.openGUI(event.getPlayer(), (Player) event.getRightClicked());
			}
		}
	}

	// Then player is below -10 he will be teleported back to spawn
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getPlayer().getLocation().getY() < -10) {
			e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
		}
	}

	// Remove potion bottle after use
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		final Player player = e.getPlayer();

		if (e.getItem().getType().equals(Material.POTION)) {
			Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				@SuppressWarnings("deprecation")
				public void run() {
					player.setItemInHand(new ItemStack(Material.AIR));
				}
			}, 1L);
		}
	}

	//////////////////////// DISABLED EVENTS ////////////////////////

	// Disable fall damage
	@EventHandler
	public void onCancelFallDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			}
		}
	}

	// Disable crafting
	@EventHandler
	public void onItemCrafting(CraftItemEvent e) {
		if (e.getWhoClicked().hasPermission("cubeplex.mod.crafting")) {
			return;
		}
		e.getWhoClicked().sendMessage(MainClassRPG.prefix + "Sorry, we disable this crafting!");
		e.setCancelled(true);
	}

	// Disable enchanting
	@EventHandler
	public void onPlayerEnchant(PrepareItemEnchantEvent e) {
		if (e.getEnchanter().hasPermission("cubeplex.mod.enchant")) {
			return;
		}
		e.getEnchanter().sendMessage(MainClassRPG.prefix + "Sorry, we disable this enchanting!");
		e.setCancelled(true);
	}

	// Disable block breaking
	@EventHandler
	public void onBlockBrake(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("cubeplex.mod.breakblock")) {
			e.setCancelled(true);
		}
	}

	// Disable block placement
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("cubeplex.mod.placeblock")) {
			e.setCancelled(true);
		}
	}

	// Disable block damage then explosion happens
	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		e.blockList().clear();
	}

	// Disable fire spread
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.getCause() == IgniteCause.SPREAD) {
			event.setCancelled(true);
		}
	}

	// Disable Weather
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
}
