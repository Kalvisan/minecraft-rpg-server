package com.github.kalvisan.extra;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.kalvisan.MainClassRPG;
import com.github.kalvisan.objects.RespawnBoxClass;

public class Timer extends BukkitRunnable  {

   public final MainClassRPG plugin;
   
   public static Map<RespawnBoxClass, Integer> boxOnTimer = new HashMap<RespawnBoxClass, Integer>();

   public Timer(MainClassRPG plugin) {
       this.plugin = plugin;
   }
	
   
	@Override
	public void run() {
		respawnBox();
	}
	
	public void respawnBox() {
		if(!boxOnTimer.isEmpty())
		for (Map.Entry<RespawnBoxClass, Integer> b : boxOnTimer.entrySet()) {
			if(b.getValue() > 0) {
				b.setValue(b.getValue() - 1);
			} else {
				Block block = b.getKey().getBlock();
				block.setType(b.getKey().getMaterial());
				boxOnTimer.remove(b.getKey());
			}
		}
	}

}
