package com.github.kalvisan.api;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction;

public class Title {

	public static void sendFullTitle(Player p, String title, String subtitle, int fadeInSec, int staySec, int fadeOutSec) {
		IChatBaseComponent titleJSON = ChatSerializer.a("{\"text\": \"" + title.replaceAll("&", "§") + "\"}");
      IChatBaseComponent subtitleJSON = ChatSerializer.a("{\"text\": \"" + subtitle.replaceAll("&", "§") + "\"}");
		PacketPlayOutTitle packet1 = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, fadeInSec * 20, staySec * 20, fadeOutSec * 20);
		PacketPlayOutTitle packet2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON, fadeInSec * 20, staySec * 20, fadeOutSec * 20);

		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet1);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet2);
	}
	

	public static void sendTitle(Player p, String title, int fadeInSec, int staySec, int fadeOutSec) {
      IChatBaseComponent titleJSON = ChatSerializer.a("{\"text\": \"" + title.replaceAll("&", "§") + "\"}");
		PacketPlayOutTitle packet1 = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, fadeInSec * 20, staySec * 20, fadeOutSec * 20);

		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet1);
	}

	public static void sendSubTitle(Player p, String subtitle, int fadeInSec, int staySec, int fadeOutSec) {
      IChatBaseComponent subtitleJSON = ChatSerializer.a("{\"text\": \"" + subtitle.replaceAll("&", "§") + "\"}");
		PacketPlayOutTitle packet1 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON, fadeInSec * 20, staySec * 20, fadeOutSec * 20);

		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet1);
	}
	
	public static void sendActionBar (Player p, String text) {
		IChatBaseComponent textJSON = ChatSerializer.a("{\"text\": \"" + text.replaceAll("&", "§") + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(textJSON, (byte) 2);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void reset(Player p) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.RESET, null));
	}

	public static void clear(Player p) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.CLEAR, null));
	}
}