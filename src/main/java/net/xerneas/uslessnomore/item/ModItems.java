package net.xerneas.uslessnomore.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xerneas.uslessnomore.UselessNoMore;
import net.xerneas.uslessnomore.block.ModBlocks;

import java.util.Optional;

public class ModItems {
    public static final Item ELDER_GUARDIAN_SPIKE = registerItem("elder_guardian_spike", new Item(new Item.Settings().rarity(Rarity.RARE)));
    public static final Item GUARDIAN_SPIKE = registerItem("guardian_spike", new Item(new Item.Settings()));
    public static final Item GUARDIAN_ARROW = registerItem("guardian_arrow", new GuardianArrowItem(new Item.Settings()));
    public static final Item LIT_TORCHFLOWER = registerItem("lit_torchflower", new BlockItem(ModBlocks.LIT_TORCHFLOWER_BLOCK, new Item.Settings()));
    public static final Item PITCHER_ACID = registerItem("pitcher_acid", new PitcherAcidItem(new Item.Settings().maxCount(16)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(UselessNoMore.MOD_ID, name), item);
    }

    public static void registerModItems() {
        UselessNoMore.LOGGER.info("Registering Mod Items for " + UselessNoMore.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(ELDER_GUARDIAN_SPIKE);
            entries.add(GUARDIAN_SPIKE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
                entries.add(GUARDIAN_ARROW);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(LIT_TORCHFLOWER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(PITCHER_ACID);
        });

        DispenserBlock.registerProjectileBehavior(ModItems.GUARDIAN_ARROW);
        DispenserBlock.registerBehavior(ModItems.PITCHER_ACID, new FallibleItemDispenserBehavior() {
            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                World world = pointer.world();
                BlockState blockState = world.getBlockState(blockPos);
                Optional<BlockState> optional = PitcherAcidItem.getOxidizeState(blockState);
                if (optional.isPresent()) {
                    world.setBlockState(blockPos, (BlockState)optional.get());
                    world.playSound(null, blockPos, SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.decrementStackWithRemainder(pointer, stack, new ItemStack(Items.GLASS_BOTTLE));
                    this.setSuccess(true);
                    return stack;
                } else {
                    return super.dispenseSilently(pointer, stack);
                }
            }
        });
    }
}
