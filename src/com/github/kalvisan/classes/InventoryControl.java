package com.github.kalvisan.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.kalvisan.MainClassRPG;

public class InventoryControl implements Listener {
	MainClassRPG plugin;
	
	public InventoryControl(MainClassRPG p) {
		this.plugin = p;
	}
	
	public static ArrayList<Material> diabledDrops = new ArrayList<>();
	

	static {
		diabledDrops.add(Material.BOOK);
		diabledDrops.add(Material.RABBIT_HIDE);
	}
	
	// Disable listed items drop
	@EventHandler
	public void onItemDropEvent(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		if (MainClassRPG.classes.containsKey(player.getUniqueId()) && e.getItemDrop().getItemStack() != null) {
			if (diabledDrops.contains(e.getItemDrop().getItemStack().getType())) {
				e.setCancelled(true);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HAT, 5, 2);
				return;
			}
		}
	}
	
	// Disable item move in inventory
	@EventHandler
	public void onInventoryControlClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		if(e.getCurrentItem() == null) return;
		
		Player player = (Player) e.getWhoClicked();
		
		if (!MainClassRPG.classes.containsKey(player.getUniqueId()) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;

		if (diabledDrops.contains(e.getCurrentItem().getType())) {
			e.setCancelled(true);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HAT, 5, 2);
			return;
		}
	}

	@EventHandler
	public void onInteractSpells(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (!MainClassRPG.classes.containsKey(player.getUniqueId())) {
			return;
		}
		if (InventoryClickItem(player.getItemInHand(), player)) {
			e.setCancelled(true);
			return;
		}
	}

	public boolean InventoryClickItem(ItemStack is, Player player) {
		if(is == null) {
			return false;
		}
		if (!is.hasItemMeta()) {
			return false;
		}
		switch (is.getType()) {
		case BOOK:
			onSpellClick(player);
			return true;
		case RABBIT_HIDE:
			if (!is.getItemMeta().getDisplayName().equalsIgnoreCase("Backpack")) {
				return false;
			}
			player.openInventory(MainClassRPG.classes.get(player.getUniqueId()).getBackpack());
			return true;
		default:
			break;
		}
		return false;
	}

	public void onSpellClick(Player player) {
		player.getWorld().spawnArrow(player.getLocation(), player.getVelocity().multiply(10D), 0f, 0f);
	}
}
