package net.xerneas.uslessnomore.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.xerneas.uslessnomore.UselessNoMore;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;

public class ModGamerules {
    public static final GameRules.Key<GameRules.BooleanRule> DO_FURNACE_MINECART_LOAD_CHUNKS = GameRuleRegistry.register("doFurnaceMinecartLoadChunks", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> DO_FURNACE_MINECART_USE_CAMPFIRE_PARTICLES = GameRuleRegistry.register("doFurnaceMinecartUseCampfireParticles", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> DO_FURNACE_MINECART_USE_ALL_FUELS = GameRuleRegistry.register("doFurnaceMinecartUseAllFuels", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntRule> FURNACE_MINECART_MAX_BURN_TIME = GameRuleRegistry.register("furnaceMinecartMaxBurnTime", GameRules.Category.MISC, GameRuleFactory.createIntRule(72000));
    public static final GameRules.Key<GameRules.IntRule> FURNACE_MINECART_SPEED = GameRuleRegistry.register("furnaceMinecartSpeed", GameRules.Category.MISC, GameRuleFactory.createIntRule(7));
    public static void registerModGamerules() {
        UselessNoMore.LOGGER.info("Registering Mod Gamerules for " + UselessNoMore.MOD_ID);
    }
}
