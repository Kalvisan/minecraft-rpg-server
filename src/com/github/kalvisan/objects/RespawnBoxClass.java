package com.github.kalvisan.objects;

import org.bukkit.Material;
import org.bukkit.block.Block;

public final class RespawnBoxClass {

	private Block block;
	private Material material;

	public RespawnBoxClass(Block block, Material material) {
		this.setBlock(block);
		this.setMaterial(material);
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
}
