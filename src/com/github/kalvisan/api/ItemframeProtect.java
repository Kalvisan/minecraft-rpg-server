package com.github.kalvisan.api;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.github.kalvisan.MainClassRPG;

public class ItemframeProtect implements Listener {

	private MainClassRPG plugin;

	public ItemframeProtect(MainClassRPG instance)
	  {
	    this.plugin = instance;
	  }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDestroyByEntity(HangingBreakByEntityEvent event) {
		if ((event.getRemover() instanceof Player)) {
			Player p = (Player) event.getRemover();
			if ((event.getEntity().getType() == EntityType.ITEM_FRAME) && (!p.hasPermission("cubeplex.mod.itemframe.destroy"))) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlaceByEntity(HangingPlaceEvent event) {
		Player p = event.getPlayer();
		if ((event.getEntity().getType() == EntityType.ITEM_FRAME) && (!p.hasPermission("cubeplex.mod.itemframe.place"))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void canRotate(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();
		if (!entity.getType().equals(EntityType.ITEM_FRAME)) {
			return;
		}
		ItemFrame iFrame = (ItemFrame) entity;
		if ((iFrame.getItem().equals(null)) || (iFrame.getItem().getType().equals(Material.AIR))) {
			return;
		}
		if ((!player.hasPermission("cubeplex.mod.itemframe.rotate"))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void ItemRemoval(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player p = (Player) e.getDamager();
			if ((e.getEntity().getType() == EntityType.ITEM_FRAME) && (!p.isOp()) && (!p.hasPermission("cubeplex.mod.itemframe.remove"))) {
				e.setCancelled(true);
			}
		}
		
		if (((e.getDamager() instanceof Projectile)) && (e.getEntity().getType() == EntityType.ITEM_FRAME)) {
			Projectile p = (Projectile) e.getDamager();
			Player player = (Player) p.getShooter();
			if ((!player.isOp()) && (!player.hasPermission("cubeplex.mod.itemframe.remove"))) {
				e.setCancelled(true);
			}
		}
	}
}
