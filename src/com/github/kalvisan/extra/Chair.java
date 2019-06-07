package com.github.kalvisan.extra;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.github.kalvisan.MainClassRPG;

import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;


public class Chair implements Listener {
	private MainClassRPG plugin;
	public Map<Player, Location> playerLocation = new HashMap<>();
	public Map<Player, Arrow> chairList = new HashMap<>();
	public Map<Player, Location> chairLocation = new HashMap<>();

	public Chair(MainClassRPG p) {
		this.plugin = p;
	}

	@EventHandler
	public void onPlayerInteractChair(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getClickedBlock() != null) {
			Block block = event.getClickedBlock();
			if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (!player.isInsideVehicle() && !player.isSneaking())) {
				if (block.getType() == Material.ACACIA_STAIRS) {
					World world = player.getWorld();
					this.playerLocation.put(player, player.getLocation());
					Arrow chair = (Arrow) world.spawnEntity(player.getLocation(), EntityType.ARROW);		
					this.chairList.put(player, chair);
					chair.teleport(block.getLocation().add(0.5D, 0.2D, 0.5D));
					this.chairLocation.put(player, chair.getLocation());
					chair.setPassenger(player);
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (this.playerLocation.containsKey(event.getPlayer())) {
			if (event.getPlayer().isSneaking()) {
				Player player = event.getPlayer();
				final Player sit_player = player;
				final Location stand_location = (Location) this.playerLocation.get(player);
				Entity sit_chair = (Entity) this.chairList.get(player);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
					public void run() {
						sit_player.teleport(stand_location);
						sit_player.setSneaking(false);
					}
				}, 1L);
				event.setCancelled(true);
				this.playerLocation.remove(player);
				sit_chair.remove();
				this.chairList.remove(player);
				event.setCancelled(true);
				return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (this.playerLocation.containsKey(event.getPlayer())) {
			Player player = event.getPlayer();
			if ((player.getLocation() != this.playerLocation.get(player)) && (!player.isInsideVehicle())) {
				World world = player.getWorld();
				if (this.chairLocation.containsKey(player)) {
					Entity chair = world.spawnEntity((Location) this.chairLocation.get(player), EntityType.ARROW);
					chair.setPassenger(player);
				}
			}
		}
	}
}
