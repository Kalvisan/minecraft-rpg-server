package com.github.kalvisan.classes.sorceress;

import org.bukkit.Location;
import org.bukkit.Material;

import com.github.kalvisan.api.ParticleEffect;

public final class WandClass {
	
	private Material wandType;
	private int cooldown;
	private ParticleEffect effect;
	private float yOffset;
	
	public WandClass(Material wandType, int cooldown, ParticleEffect effect, float yOffset) {
		this.setWandType(wandType);
		this.setCooldown(cooldown);
		this.setEffect(effect);
		this.yOffset = yOffset;
	}

	public Material getWandType() {
		return wandType;
	}

	public void setWandType(Material wandType) {
		this.wandType = wandType;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public ParticleEffect getEffect() {
		return effect;
	}

	public void setEffect(ParticleEffect effect) {
		this.effect = effect;
	}
	
	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}
	
	public void displayParticleEffect(Location loc) {
		this.effect.display(0F, this.yOffset, 0F, 0F, 1, loc, 100D);
	}
	
	
}
