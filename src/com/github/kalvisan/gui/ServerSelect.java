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

public class ServerSelect implements Listener {
	MainClassRPG plugin;

	public static Inventory gui = Bukkit.createInventory(null, 9 * 3, "Server selector");
	static ItemStack s1 = new ItemStack(Material.DIAMOND_CHESTPLATE);
	static ItemStack s2 = new ItemStack(Material.BOW);
	static ItemStack s3 = new ItemStack(Material.GRASS);
	static ItemStack s4 = new ItemStack(Material.ENDER_PEARL);

	static ItemStack back = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);

	static {
		ItemMeta b = back.getItemMeta();
		b.setDisplayName(" ");
		back.setItemMeta(b);

		ItemMeta im1 = s1.getItemMeta();
		im1.setDisplayName("§a§lFaction server");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§4Last Reset: §cSoon");
		lore.add(" ");
		lore.add("§6Server Features:");
		lore.add("§f- §c§lCannon Raiding");
		lore.add("§f- McMMO");
		lore.add("§f- Custom World");
		lore.add("§f- Custom Enchantments");
		lore.add("§f- Nether and End Worlds");
		lore.add("§f- Amazing voting rewards");
		lore.add("§f- and much, much more!");
		lore.add(" ");
		lore.add("§eClick to connect to §c§lFaction");
		im1.setLore(lore);
		s1.setItemMeta(im1);

		ItemMeta im2 = s2.getItemMeta();
		im2.setDisplayName("§5§lRPG server");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(" ");
		lore2.add("§6Server Features:");
		lore2.add("§f- §c§lDIABLO alike RPG");
		lore2.add("§f- Classes");
		lore2.add("§f- Spells, skills, abilitys");
		lore2.add("§f- Amazing RPG experience");
		lore2.add("§f- and much, much more!");
		lore2.add(" ");
		lore2.add("§eClick to connect to §5§lMMORPG");
		im2.setLore(lore2);
		s2.setItemMeta(im2);

		ItemMeta im3 = s3.getItemMeta();
		im3.setDisplayName("§a§lSkyBlock server");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(" ");
		lore3.add("§6Server Features:");
		lore3.add("§f- §b§lASkyblock");
		lore3.add("§f- Stable economic");
		lore3.add("§f- Extra islands");
		lore3.add("§f- Great crate items");
		lore3.add("§f- and much, much more!");
		lore3.add(" ");
		lore3.add("§eClick to connect to §b§lSkyBlock");
		im3.setLore(lore3);
		s3.setItemMeta(im3);

		ItemMeta im4 = s4.getItemMeta();
		im4.setDisplayName("§a§lFunPvP server");
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(" ");
		lore4.add("§6Comming soon...");
		im4.setLore(lore4);
		s4.setItemMeta(im4);

		gui.setItem(0, back);
		gui.setItem(1, back);
		gui.setItem(2, back);
		gui.setItem(3, back);
		gui.setItem(4, back);
		gui.setItem(5, back);
		gui.setItem(6, back);
		gui.setItem(7, back);
		gui.setItem(8, back);

		gui.setItem(9, back);
		gui.setItem(10, s1);
		gui.setItem(11, back);
		gui.setItem(12, s2);
		gui.setItem(13, back);
		gui.setItem(14, s3);
		gui.setItem(15, back);
		gui.setItem(16, s4);
		gui.setItem(17, back);

		gui.setItem(18, back);
		gui.setItem(19, back);
		gui.setItem(20, back);
		gui.setItem(21, back);
		gui.setItem(22, back);
		gui.setItem(23, back);
		gui.setItem(24, back);
		gui.setItem(25, back);
		gui.setItem(26, back);
	}

	public ServerSelect(MainClassRPG p) {
		this.plugin = p;
	}

	public static void openGUI(Player p) {
		if (p.hasPermission("lobby.player.gui")) {
			p.openInventory(gui);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (e.getInventory().getName().equalsIgnoreCase(gui.getName()) && c != null) {

			if (c.getType() == Material.DIAMOND_CHESTPLATE) {
				e.setCancelled(true);
				p.closeInventory();

				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "sudo " + p.getName() + " faction");
			}

			if (c.getType() == Material.BOW) {
				e.setCancelled(true);
				p.closeInventory();

				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "sudo " + p.getName() + " rpg");
			}

			if (c.getType() == Material.GRASS) {
				e.setCancelled(true);
				p.closeInventory();

				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "sudo " + p.getName() + " skyblock");
			}

			if (c.getType() == Material.ENDER_PEARL) {
				e.setCancelled(true);
				p.closeInventory();

				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "sudo " + p.getName() + " funpvp");
			}

			if (c.getType() == Material.SKULL_ITEM) {
				e.setCancelled(true);
				p.closeInventory();

			}

			e.setCancelled(true);
			p.closeInventory();
		}
	}
}