package com.github.kalvisan.objects;

public enum Classes {

	BARBARIAN("Barbarian", "The Barbarian is a breed of Fighter focused more on damage"),
	ARCHER("Archer", "Rangers are woodsmen skilled at surviving in the wild"),
	SORCERESS("Sorceress", "Born with magical abilities"),
	PALADIN("Paladin", "Fighter with the power of healing magic and buffs for their allies.");

	private String name;
	private String description;

	Classes(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
}
