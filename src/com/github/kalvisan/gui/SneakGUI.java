package com.github.kalvisan.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.kalvisan.MainClassRPG;

public class SneakGUI implements Listener {

	public SneakGUI() {}
	
	public static Inventory gui = Bukkit.createInventory(null, 9 * 1, "Player settings");
	static ItemStack s1 = new ItemStack(Material.BANNER);
	static ItemStack s2 = new ItemStack(Material.COMPASS);
	static ItemStack s3 = new ItemStack(Material.CHEST);
	static ItemStack s4 = new ItemStack(Material.GOLD_INGOT);
	static ItemStack s5 = new ItemStack(Material.SHIELD);

	static HashMap<Player, Player> sneakData = new HashMap<Player, Player>();

	static {
		ItemMeta im = s1.getItemMeta();
		im.setDisplayName("§a§lInvite to guild");
		s1.setItemMeta(im);

		ItemMeta im2 = s2.getItemMeta();
		im2.setDisplayName("§e§lInvite to party");
		s2.setItemMeta(im2);

		ItemMeta im3 = s3.getItemMeta();
		im3.setDisplayName("§7§lInspect inventory");
		s3.setItemMeta(im3);

		ItemMeta im4 = s4.getItemMeta();
		im4.setDisplayName("§7§lTrade with player");
		s4.setItemMeta(im4);

		ItemMeta im5 = s5.getItemMeta();
		im5.setDisplayName("§c§lDuel a player");
		s5.setItemMeta(im5);

		gui.setItem(2, s1);
		gui.setItem(3, s2);
		gui.setItem(4, s3);
		gui.setItem(5, s4);
		gui.setItem(6, s5);

	}

	public static void openGUI(Player p, Player clicked) {
		if (p.hasPermission("cubeplex.player.sneakGUI")) {
			sneakData.put(p, clicked);
			p.openInventory(gui);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (e.getInventory().getName().equalsIgnoreCase(gui.getName()) && c != null) {
			ItemStack items[] = sneakData.get(p).getInventory().getContents();
			// TODO: inv, not opening
			if (c.equals(s3)) {
				Inventory inside = Bukkit.createInventory(null, 9 * 5, gui.getName());
				int i = 0;
				for (ItemStack armour : items) {
					if (armour != null) {
						inside.setItem(i, armour);
					}
					i++;
				}
				sneakData.remove(p);
				e.setCancelled(true);
				p.openInventory(inside);
			} else if (c.equals(s1)) {
				p.sendMessage(MainClassRPG.prefix + "Not created yet!");
			} else if (c.equals(s2)) {
				p.sendMessage(MainClassRPG.prefix + "Not created yet!");
			} else if (c.equals(s4)) {
				p.sendMessage(MainClassRPG.prefix + "Not created yet!");
			}
			sneakData.remove(p);
			p.closeInventory();
			e.setCancelled(true);
		}
	}
}