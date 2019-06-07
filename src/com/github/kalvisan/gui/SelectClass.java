package com.github.kalvisan.gui;

import java.util.ArrayList;

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
import com.github.kalvisan.objects.Classes;

public class SelectClass implements Listener{
	MainClassRPG plugin;

	public static Inventory gui = Bukkit.createInventory(null, 9 * 1, "Select a Class");
	static ItemStack s1 = new ItemStack(Material.WOOD_AXE);
	static ItemStack s2 = new ItemStack(Material.WOOD_SWORD);
	static ItemStack s3 = new ItemStack(Material.BOW);
	static ItemStack s4 = new ItemStack(Material.WOOD_HOE);
	
	static ItemStack back = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);

	static {
		ItemMeta b = back.getItemMeta();
		b.setDisplayName(" ");
		back.setItemMeta(b);
		
		
		ItemMeta im1 = s1.getItemMeta();
		im1.setDisplayName("§e§lBarbarian");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add("§7§lDefence: §eMedium");
		lore.add("§7§lDamage: §aHigh");
		lore.add("§7§lRange: §cShort");
		lore.add(" ");
		lore.add("§6§lMelee");
		lore.add("§7Class can be switched anytime");
		im1.setLore(lore);
		s1.setItemMeta(im1);

		ItemMeta im2 = s2.getItemMeta();
		im2.setDisplayName("§e§lPaladin");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(" ");
		lore2.add("§7§lDefence: §aVery high");
		lore2.add("§7§lDamage: §eMedium");
		lore2.add("§7§lRange: §cShort");
		lore2.add(" ");
		lore2.add("§6§lMelee");
		lore2.add("§7Class can be switched anytime");
		im2.setLore(lore2);
		s2.setItemMeta(im2);

		ItemMeta im3 = s3.getItemMeta();
		im3.setDisplayName("§e§lAmazone");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(" ");
		lore3.add("§7§lDefence: §cLow");
		lore3.add("§7§lDamage: §aHigh");
		lore3.add("§7§lRange: §aVery long");
		lore3.add(" ");
		lore3.add("§6§lPhysical Ranged");
		lore3.add("§7Class can be switched anytime");
		im3.setLore(lore3);
		s3.setItemMeta(im3);

		ItemMeta im4 = s4.getItemMeta();
		im4.setDisplayName("§e§lSorceress");
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(" ");
		lore4.add("§7§lDefence: §cVery low");
		lore4.add("§7§lDamage: §aHigh");
		lore4.add("§7§lRange: §eMedium");
		lore4.add(" ");
		lore4.add("§6§lMagic Ranged");
		lore4.add("§7Class can be switched anytime");
		im4.setLore(lore4);
		s4.setItemMeta(im4);

		gui.setItem(0, back);
		gui.setItem(1, back);

		gui.setItem(2, s1);
		gui.setItem(3, s2);
		gui.setItem(4, s3);
		gui.setItem(5, s4);
		
		gui.setItem(6, back);
		gui.setItem(7, back);
		gui.setItem(8, back);
		
	}

	public SelectClass(MainClassRPG p) {
		this.plugin = p;
	}

	public static void openGUI(Player p) {
		if (p.hasPermission("cubeplex.player.select")) {
			p.openInventory(gui);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (e.getInventory().getName().equalsIgnoreCase(gui.getName()) && c != null) {

			if (c.getType() == Material.WOOD_AXE) {
				e.setCancelled(true);
				p.closeInventory();
				
				p.chat("/role class " + Classes.BARBARIAN.getName());
			}

			if (c.getType() == Material.WOOD_SWORD) {
				e.setCancelled(true);
				p.closeInventory();
				
				p.chat("/role class " + Classes.PALADIN.getName());
			}

			if (c.getType() == Material.BOW) {
				e.setCancelled(true);
				p.closeInventory();
				
				p.chat("/role class " + Classes.ARCHER.getName());
			}

			if (c.getType() == Material.WOOD_HOE) {
				e.setCancelled(true);
				p.closeInventory();
				
				p.chat("/role class " + Classes.SORCERESS.getName());
			}
			
			if (c.getType() == Material.WOOD_SPADE) {
				e.setCancelled(true);
				p.closeInventory();
				// TODO: Add new class who use Shovel
			}

			e.setCancelled(true);
			p.closeInventory();
		}
	}
}
