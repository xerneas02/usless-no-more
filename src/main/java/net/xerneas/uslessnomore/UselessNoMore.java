package net.xerneas.uslessnomore;

import net.fabricmc.api.ModInitializer;


import net.xerneas.uslessnomore.block.ModBlocks;
import net.xerneas.uslessnomore.entities.ModEntities;
import net.xerneas.uslessnomore.gamerule.ModGamerules;
import net.xerneas.uslessnomore.item.ModItems;
import net.xerneas.uslessnomore.recipe.ModRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UselessNoMore implements ModInitializer {
	public static final String MOD_ID = "useless-no-more";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModEntities.registerModEntities();
		ModBlocks.registerModBlocks();
		ModRecipes.registerMosRecipes();
		ModGamerules.registerModGamerules();
	}
}