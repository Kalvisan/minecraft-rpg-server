package com.github.kalvisan.classes.sorceress;

import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.kalvisan.MainClassRPG;
import com.github.kalvisan.api.ParticleEffect;

public class Wand implements Listener {
	private MainClassRPG plugin;
	private Hashtable<String, Long> Timers = new Hashtable<>();
	static ArrayList<WandClass> wands = new ArrayList<>();

	static {
		wands.add(new WandClass(Material.WOOD_HOE, 1000, ParticleEffect.FIREWORKS_SPARK, 0.3F));
		wands.add(new WandClass(Material.STONE_HOE, 950, ParticleEffect.FIREWORKS_SPARK, 0.2F));
		wands.add(new WandClass(Material.IRON_HOE, 900, ParticleEffect.CLOUD, 0.3F));
		wands.add(new WandClass(Material.GOLD_HOE, 800, ParticleEffect.FLAME, 0.4F));
		wands.add(new WandClass(Material.DIAMOND_HOE, 700, ParticleEffect.CRIT_MAGIC, 0.3F));
	}

	public Wand(MainClassRPG instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onPlayerUsingItem(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {

			for (WandClass w : wands) {
				if (player.getItemInHand().getType().equals(w.getWandType())) {
					event.setCancelled(true);

					Long last = (Long) this.Timers.get(player.getName());
					int cooldown = w.getCooldown();
					long current = System.currentTimeMillis();
					if ((last == null) || (last.longValue() + cooldown <= current)) {
						double speed = 1D;
						final Snowball snowball = (Snowball) player.launchProjectile(Snowball.class);
						snowball.setShooter(player);
						snowball.setVelocity(snowball.getVelocity().multiply(speed));

						this.Timers.put(player.getName(), Long.valueOf(current));

						new BukkitRunnable() {
							int t = 8;

							public void run() {
								w.displayParticleEffect(snowball.getLocation());
								if (this.t <= 0 || snowball.isDead()) {
									ParticleEffect.EXPLOSION_NORMAL.display(0F, 0F, 0F, 0F, 1, snowball.getLocation(), 100D);
									snowball.remove();
									cancel();
									return;
								}
								this.t -= 1;
							}
						}.runTaskTimer(plugin, 1L, 0);
					}
				}
			}

		}
	}

}
