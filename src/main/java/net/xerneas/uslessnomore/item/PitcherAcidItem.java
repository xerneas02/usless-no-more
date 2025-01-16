package net.xerneas.uslessnomore.item;

import com.google.common.collect.BiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PitcherAcidItem extends PotionItem {

    public static final Map<Block, Block> OXIDATION_MAP = new HashMap<>();

    static {
        // Plain copper blocks
        OXIDATION_MAP.put(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER);
        OXIDATION_MAP.put(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER);
        OXIDATION_MAP.put(Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER);

        // Cut copper variants
        OXIDATION_MAP.put(Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER);
        OXIDATION_MAP.put(Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER);
        OXIDATION_MAP.put(Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER);

        // Cut copper stairs variants
        OXIDATION_MAP.put(Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS);
        OXIDATION_MAP.put(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS);
        OXIDATION_MAP.put(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS);

        // Cut copper slab variants
        OXIDATION_MAP.put(Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB);
        OXIDATION_MAP.put(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB);
        OXIDATION_MAP.put(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB);

        // Modded: Chiseled copper variants
        OXIDATION_MAP.put(Blocks.CHISELED_COPPER, Blocks.EXPOSED_CHISELED_COPPER);
        OXIDATION_MAP.put(Blocks.EXPOSED_CHISELED_COPPER, Blocks.WEATHERED_CHISELED_COPPER);
        OXIDATION_MAP.put(Blocks.WEATHERED_CHISELED_COPPER, Blocks.OXIDIZED_CHISELED_COPPER);

        // Modded: Copper grate variants
        OXIDATION_MAP.put(Blocks.COPPER_GRATE, Blocks.EXPOSED_COPPER_GRATE);
        OXIDATION_MAP.put(Blocks.EXPOSED_COPPER_GRATE, Blocks.WEATHERED_COPPER_GRATE);
        OXIDATION_MAP.put(Blocks.WEATHERED_COPPER_GRATE, Blocks.OXIDIZED_COPPER_GRATE);

        // Modded: Copper door variants
        OXIDATION_MAP.put(Blocks.COPPER_DOOR, Blocks.EXPOSED_COPPER_DOOR);
        OXIDATION_MAP.put(Blocks.EXPOSED_COPPER_DOOR, Blocks.WEATHERED_COPPER_DOOR);
        OXIDATION_MAP.put(Blocks.WEATHERED_COPPER_DOOR, Blocks.OXIDIZED_COPPER_DOOR);

        // Modded: Copper trapdoor variants
        OXIDATION_MAP.put(Blocks.COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR);
        OXIDATION_MAP.put(Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR);
        OXIDATION_MAP.put(Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR);

        // Modded: Copper bulb variants
        OXIDATION_MAP.put(Blocks.COPPER_BULB, Blocks.EXPOSED_COPPER_BULB);
        OXIDATION_MAP.put(Blocks.EXPOSED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB);
        OXIDATION_MAP.put(Blocks.WEATHERED_COPPER_BULB, Blocks.OXIDIZED_COPPER_BULB);
    }


    public PitcherAcidItem(Settings settings) {
        super(settings);
    }


    public static Optional<BlockState> getOxidizeState(BlockState state) {
        return Optional.ofNullable((Block)(OXIDATION_MAP.get(state.getBlock()))).map(block -> block.getStateWithProperties(state));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WITHER,
                    200,
                    0
            ));
        }

        return result;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (OXIDATION_MAP.containsKey(state.getBlock())) {
            if (!world.isClient) {
                PlayerEntity player = context.getPlayer();
                ItemStack stack = context.getStack();

                // Get the next block in the oxidation chain
                Block nextBlock = OXIDATION_MAP.get(state.getBlock());

                // Preserve the block's properties when oxidizing
                BlockState nextState = nextBlock.getDefaultState();
                for (Property<?> property : state.getProperties()) {
                    nextState = applyProperty(nextState, property, state);
                }


                // Set the block with preserved state
                world.setBlockState(pos, nextState, 3);

                // Play sound effect
                world.playSound(
                        null,
                        pos,
                        SoundEvents.BLOCK_SLIME_BLOCK_BREAK,
                        SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                );

                // Consume the Pitcher Acid
                stack.decrement(1);

                // Give an empty bottle to the player
                if (player != null && !player.isCreative()) {
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);

                    if (!player.getInventory().insertStack(emptyBottle)) {
                        player.dropItem(emptyBottle, false);
                    }
                }
            }
            return ActionResult.success(world.isClient);
        }

        return super.useOnBlock(context);
    }

    private static <T extends Comparable<T>> BlockState applyProperty(BlockState state, Property<T> property, BlockState originalState) {
        return state.with(property, originalState.get(property));
    }


}
