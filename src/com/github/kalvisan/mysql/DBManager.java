package com.github.kalvisan.mysql;

import java.sql.SQLException;
import java.util.logging.Level;

import com.github.kalvisan.MainClassRPG;

public class DBManager implements Runnable {
	private final MainClassRPG plugin;
	private MySQL db;

	public DBManager(MainClassRPG pl) {
		this.plugin = pl;
		Thread thread = new Thread(this);
		thread.start();
	}

	public void startDB() {
		if (!plugin.getConfig().getBoolean("mysql.enable")) {
			return;
		}
		String hostname = plugin.getConfig().getString("mysql.host");
		String port = plugin.getConfig().getString("mysql.port");
		String username = plugin.getConfig().getString("mysql.username");
		String password = plugin.getConfig().getString("mysql.password");

		this.db = new MySQL(hostname, port, username, password);
		try {
			this.db.openConnection();
			// Statement state = this.db.getConnection().createStatement();
			// state.executeUpdate("CRATE TABLE IF NOT EXISTS ''");

		} catch (ClassNotFoundException | SQLException e) {
			MainClassRPG.log("Can't connect to DB!", Level.WARNING);
			MainClassRPG.log(e.toString(), Level.INFO);
		}
	}

	public void closeDB() {
		if (!plugin.getConfig().getBoolean("mysql.enable")) {
			return;
		}
		try {
			this.db.closeConnection();
		} catch (SQLException e) {
			MainClassRPG.log("Can't disable DB!", Level.WARNING);
			MainClassRPG.log(e.toString(), Level.INFO);
		}
	}

	@Override
	public void run() {

	}

}
