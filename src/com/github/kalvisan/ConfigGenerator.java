package com.github.kalvisan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigGenerator {

	MainClassRPG plugin;
	String path;

	public ConfigGenerator(MainClassRPG instance) {
		this.plugin = instance;
		path = plugin.pluginPath();
	}

	public void CrateDefaultConfig() {
		File configFile = new File(path, "config.yml");
		if (!configFile.exists()) {
			plugin.getLogger().info(plugin.prefix + "Creating default config.yml");
			plugin.getDataFolder().mkdirs();
			copy(plugin.getResource("config.yml"), configFile);
			plugin.reloadConfig();
		} else {
			FileConfiguration config = plugin.getConfig();
			if (!plugin.getDescription().getVersion().equalsIgnoreCase(config.getString("version"))) {
				System.out.println(config.getString("version"));
				LocalDate today = LocalDate.now();
				configFile.renameTo(new File(plugin.getDataFolder(), "config_backup_" + today + ".yml"));
				copy(plugin.getResource("config.yml"), configFile);
				plugin.reloadConfig();
				plugin.getLogger().log(Level.WARNING, plugin.prefix + "config.yml has been updated! All options have been reset to default.");
			}
		}
	}

	private void copy(InputStream from, File to) {
		try {
			OutputStream out = new FileOutputStream(to);
			byte[] buffer = new byte[1024];
			int size = 0;
			while ((size = from.read(buffer)) != -1) {
				out.write(buffer, 0, size);
			}
			out.close();
			from.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
