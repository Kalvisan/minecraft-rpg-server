package com.github.kalvisan.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.kalvisan.MainClassRPG;

public class CheckItemLore implements Listener {
	MainClassRPG plugin;

	public CheckItemLore(MainClassRPG p) {
		this.plugin = p;
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = (Player) event.getPlayer();
		ItemStack item = player.getItemInHand();

		if (MainClassRPG.classes.containsKey(player.getUniqueId()) && item != null)
			if (!checkItemLore(player, item, MainClassRPG.classes.get(player.getUniqueId()).getHero().getName())) {
				event.setCancelled(true);
				swapItem(player, item);
				return;
			}
	}
	
	@EventHandler
	public void onPlayerClickItem(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		// TODO: fix on place item disable Armor inventory
		if (MainClassRPG.classes.containsKey(player.getUniqueId()) && item != null)
			if (!checkItemLore(player, item, MainClassRPG.classes.get(player.getUniqueId()).getHero().getName())) {
				if(!e.getAction().equals(InventoryAction.PICKUP_ALL)) {
					e.setCancelled(true);
					return;	
				}
			}
	}

	public void swapItem(Player player, ItemStack item) {
		boolean sucess = false;
		ItemStack[] items = player.getInventory().getContents();
		for (int x = 9; x <= 35; x++) {
			if (items[x] == null) {
				player.getInventory().setItem(x, item);
				player.setItemInHand(null);
				sucess = true;
				break;
			}
		}

		if (!sucess) {
			player.getWorld().dropItem(player.getLocation(), item);
			player.setItemInHand(null);
			player.sendMessage(MainClassRPG.prefix + ChatColor.RED + "Your backpack is full! Item is dropped.");
		}

		player.updateInventory();
		player.sendMessage(MainClassRPG.prefix + ChatColor.RED + "You can't use that item!");
	}

	
	
	public boolean checkItemLore(Player player, ItemStack item, String heroClassName) {
		if ((item != null) && (item.hasItemMeta()) && (item.getItemMeta().hasLore())) {
			List<String> itemLore = item.getItemMeta().getLore();
			for (String line : itemLore) {
				String lore = ChatColor.stripColor(line);

				if (lore.startsWith("Level:")) {
					if (Integer.parseInt(lore.split("\\+")[0].replaceAll("[^0-9.+-]", "")) > player.getLevel()) {
						return false;
					}
				}
				if (lore.startsWith("Class:")) {
					String value = ChatColor.stripColor(lore).substring(("Class:").length()).trim().replaceAll(" ", "");
					if (!value.toLowerCase().contains(heroClassName.toLowerCase()))
						return false;
				}
			}
		}
		return true;
	}

	public boolean swapItem(Inventory inv, ItemStack item) {
		for (int i = 9; i <= 35; i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, item);
				return true;
			}
		}
		return false;
	}

}
