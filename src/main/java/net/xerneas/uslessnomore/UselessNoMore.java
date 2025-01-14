package net.xerneas.uslessnomore;

import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.xerneas.uslessnomore.block.ModBlocks;
import net.xerneas.uslessnomore.entities.ModEntities;
import net.xerneas.uslessnomore.item.ModItems;
import net.xerneas.uslessnomore.potion.ModPotions;
import net.xerneas.uslessnomore.recipe.ModRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UselessNoMore implements ModInitializer {
	public static final String MOD_ID = "useless-no-more";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final RegistryKey<LootTable> ELDER_GUARDIAN_TABLE_ID = EntityType.ELDER_GUARDIAN.getLootTableId();
	private static final RegistryKey<LootTable> GUARDIAN_TABLE_ID = EntityType.GUARDIAN.getLootTableId();
	private static final RegistryKey<LootTable> TORCH_FLOWER_CROP_TABLE_ID = Blocks.TORCHFLOWER_CROP.getLootTableKey();

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModEntities.registerModEntities();
		ModBlocks.registerModBlocks();
		ModRecipes.registerMosRecipes();

		// Add item to entities loot tables
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (source.isBuiltin() && ELDER_GUARDIAN_TABLE_ID.equals(key)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.rolls(UniformLootNumberProvider.create(0, 2))
						.with(ItemEntry.builder(ModItems.ELDER_GUARDIAN_SHARD));

				tableBuilder.pool(poolBuilder);
			}
		});

		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (source.isBuiltin() && GUARDIAN_TABLE_ID.equals(key)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.rolls(UniformLootNumberProvider.create(0, 4))
						.with(ItemEntry.builder(ModItems.GUARDIAN_SHARD));

				tableBuilder.pool(poolBuilder);
			}
		});

	}
}