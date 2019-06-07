package com.github.kalvisan.extra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import com.github.kalvisan.MainClassRPG;
import com.github.kalvisan.api.ParticleEffect;
import com.github.kalvisan.objects.RespawnBoxClass;

public class ExtraBlocks implements Listener {

	public MainClassRPG plugin;
	ArrayList<Location> targets = null;

	public ExtraBlocks(MainClassRPG instance) {
		this.plugin = instance;
		targets = new ArrayList<Location>();
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		saveHashmap();
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		targets.clear();
		loadHashmap();
	}

	public boolean extraItems(Block block, Player player) {

		if (block.getType().equals(Material.MELON_BLOCK)) {
			playerHitBox(block, player, Material.MELON, 1);
			return true;
		} else if (block.getType().equals(Material.PUMPKIN)) {
			playerHitBox(block, player, Material.PUMPKIN_PIE, 1);
			return true;
		} else if (block.getType().equals(Material.TRAPPED_CHEST)) {
			// Not showing effect
			playerHitBox(block, player, Material.AIR, 1);
			// TODO: make random item drop from trapped_chest
			return true;
		}

		return false;
	}

	/////////////////////////////////////////////////////

	public void playerHitDummy(Block b, Player player) {
		if (!b.getType().equals(Material.HAY_BLOCK)) {
			return;
		}

		Location loc = b.getLocation().add(0.5, 0.5, 0.5);

		b.setType(Material.FENCE);
		ParticleEffect.CLOUD.display(0F, 0F, 0F, 0.05F, 10, loc, 100D);

		Timer.boxOnTimer.put(new RespawnBoxClass(b, Material.HAY_BLOCK), 2);
	}

	public void playerHitBox(Block block, Player player, Material mat, int amount) {
		Location loc = block.getLocation().add(0.5, 0.5, 0.5);
		Material blockMaterial = block.getType();

		block.setType(Material.AIR);
		player.getWorld().dropItem(loc, new ItemStack(mat, amount));

		ParticleEffect.CLOUD.display(0F, 0F, 0F, 0.03F, 5, loc, 50D);
		Timer.boxOnTimer.put(new RespawnBoxClass(block, blockMaterial), 100);
	}

	//////////////////////////////////////////////////////////////

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		if (!(event.getEntity() instanceof Projectile)) {
			return;
		}
		Projectile proj = event.getEntity();
		if (proj.getShooter() == null) {
			return;
		}
		if (!((Entity) proj.getShooter() instanceof Player)) {
			return;
		}
		Player p = (Player) proj.getShooter();
		World world = proj.getWorld();
		BlockIterator bi = new BlockIterator(world, proj.getLocation().toVector(), proj.getVelocity().normalize(), 0.0D, 4);
		Block hit = null;
		while (bi.hasNext()) {
			hit = bi.next();
			if (hit.getTypeId() != 0) {
				break;
			}
		}

		if (extraItems(hit, p)) {
			return;
		}

		if (this.targets.contains(hit.getLocation())) {
			proj.remove();
			playerHitDummy(hit, p);
		}

	}

	@EventHandler
	public void onPlayerHitBlock(BlockDamageEvent e) {
		if (extraItems(e.getBlock(), e.getPlayer())) {
			return;
		}
		if (this.targets.contains(e.getBlock().getLocation())) {
			playerHitDummy(e.getBlock(), e.getPlayer());
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!this.targets.contains(event.getBlock().getLocation())) {
			return;
		}
		this.targets.remove(event.getBlock().getLocation());
		MainClassRPG.log("[TestDummy] block are removed.", Level.WARNING);
	}

	//////////////////////////////////////////////////////////////

	@EventHandler
	public void onPlayerSettingTarget(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if ((item.getType().equals(Material.LEATHER)) && item.hasItemMeta())
				if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Target")) {
					Block b = event.getClickedBlock();

					targets.add(b.getLocation());
					player.sendMessage(MainClassRPG.prefix + "Target added! :)");
					player.getInventory().remove(item);
				}
		}
	}

	public void saveHashmap() {
		String pluginFolder = this.plugin.getDataFolder().getAbsolutePath();
		File file = new File(pluginFolder + File.separator + "targets.txt");
		try {
			if (!file.exists()) {
				file.mkdir();
				file.createNewFile();
				MainClassRPG.log("[TestDummy] creating new file '" + file.getAbsolutePath() + "'.", Level.INFO);
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Location l : this.targets) {
				bw.write(l.getWorld().getName() + "!" + l.getX() + "!" + l.getY() + "!" + l.getZ());
				bw.newLine();
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			MainClassRPG.log("[TestDummy] File '" + file.getAbsolutePath() + "' not found.", Level.WARNING);
		}
	}

	public void loadHashmap() {
		String pluginFolder = this.plugin.getDataFolder().getAbsolutePath();
		File file = new File(pluginFolder + File.separator + "targets.txt");
		if (file.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line = "";
				while ((line = br.readLine()) != null) {
					String[] locs = line.split("!");
					String w = locs[0];
					double x = Float.valueOf(locs[1]);
					double y = Float.valueOf(locs[2]);
					double z = Float.valueOf(locs[3]);
					this.targets.add(new Location(this.plugin.getServer().getWorld(w), x, y, z));
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
