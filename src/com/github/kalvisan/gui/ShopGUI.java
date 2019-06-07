package com.github.kalvisan.gui;

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

public class ShopGUI implements Listener {
	MainClassRPG plugin;

	public static Inventory gui = Bukkit.createInventory(null, 9 * 3, "CubePlex Shop");
	static ItemStack s1 = new ItemStack(Material.GOLD_BLOCK);
	static ItemStack s2 = new ItemStack(Material.DIAMOND_BLOCK);
	static ItemStack s3 = new ItemStack(Material.TRIPWIRE_HOOK);
	static ItemStack s4 = new ItemStack(Material.IRON_FENCE);
	static ItemStack shop = new ItemStack(Material.EXP_BOTTLE);

	static ItemStack back = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);

	static {
		ItemMeta b = back.getItemMeta();
		b.setDisplayName(" ");
		back.setItemMeta(b);

		ItemMeta sh = shop.getItemMeta();
		sh.setDisplayName("§a§lShop Link");
		shop.setItemMeta(sh);

		ItemMeta im = s1.getItemMeta();
		im.setDisplayName("§a§lBuy §6§lVIP");
		s1.setItemMeta(im);

		ItemMeta im2 = s2.getItemMeta();
		im2.setDisplayName("§a§lBuy §6§lGrandVIP");
		s2.setItemMeta(im2);

		ItemMeta im3 = s3.getItemMeta();
		im3.setDisplayName("§a§lBuy §6§lLegendary Key");
		s3.setItemMeta(im3);

		ItemMeta im4 = s4.getItemMeta();
		im4.setDisplayName("§a§lBuy §8§lUnjail");
		s4.setItemMeta(im4);

		gui.setItem(0, back);
		gui.setItem(1, s1);
		gui.setItem(2, back);
		gui.setItem(3, s2);
		gui.setItem(4, back);
		gui.setItem(5, s3);
		gui.setItem(6, back);
		gui.setItem(7, s4);
		gui.setItem(8, back);

		gui.setItem(9, back);
		gui.setItem(10, back);
		gui.setItem(11, back);
		gui.setItem(12, back);
		gui.setItem(13, back);
		gui.setItem(14, back);
		gui.setItem(15, back);
		gui.setItem(16, back);
		gui.setItem(17, back);

		gui.setItem(18, back);
		gui.setItem(19, back);
		gui.setItem(20, back);
		gui.setItem(21, back);
		gui.setItem(22, shop);
		gui.setItem(23, back);
		gui.setItem(24, back);
		gui.setItem(25, back);
		gui.setItem(26, back);
	}

	public ShopGUI(MainClassRPG p) {
		this.plugin = p;
	}

	public static void openGUI(Player p) {
		if (p.hasPermission("cubeplex.player.shopgui")) {
			p.openInventory(gui);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack c = e.getCurrentItem();
		if (e.getInventory().getName().equalsIgnoreCase(gui.getName()) && c != null) {

			if (c.getType() == Material.GOLD_BLOCK) {
				e.setCancelled(true);
				p.closeInventory();

				p.sendMessage("");
				p.sendMessage("§a§lYour link: §chttp://www.cubeplex.eu/shop/checkout.php?item=vip1&player=" + p.getName());
				p.sendMessage("");

			}

			if (c.getType() == Material.DIAMOND_BLOCK) {
				e.setCancelled(true);
				p.closeInventory();

				p.sendMessage("");
				p.sendMessage("§a§lYour link: §chttp://www.cubeplex.eu/shop/checkout.php?item=vip2&player=" + p.getName());
				p.sendMessage("");
			}

			if (c.getType() == Material.TRIPWIRE_HOOK) {
				e.setCancelled(true);
				p.closeInventory();

				p.sendMessage("");
				p.sendMessage("§a§lYour link: §chttp://www.cubeplex.eu/shop/checkout.php?item=key&player=" + p.getName());
				p.sendMessage("");
			}

			if (c.getType() == Material.IRON_FENCE) {
				e.setCancelled(true);
				p.closeInventory();

				p.sendMessage("");
				p.sendMessage("§a§lYour link: §chttp://www.cubeplex.eu/shop/checkout.php?item=unjail&player=" + p.getName());
				p.sendMessage("");
			}

			if (c.getType() == Material.EXP_BOTTLE) {
				e.setCancelled(true);
				p.closeInventory();

				p.sendMessage("");
				p.sendMessage("§a§lShop link: §chttp://www.cubeplex.eu/shop/index.php");
				p.sendMessage("");
			}

			e.setCancelled(true);
			p.closeInventory();
		}
	}
}
