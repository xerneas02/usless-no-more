package net.xerneas.uslessnomore.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class PitcherAcidItem extends Item {

    private static final Map<Block, Block> OXIDATION_MAP = new HashMap<>();

    static {

        // Plain copper blocks
        OXIDATION_MAP.put(net.minecraft.block.Blocks.COPPER_BLOCK, net.minecraft.block.Blocks.EXPOSED_COPPER);
        OXIDATION_MAP.put(net.minecraft.block.Blocks.EXPOSED_COPPER, net.minecraft.block.Blocks.WEATHERED_COPPER);
        OXIDATION_MAP.put(net.minecraft.block.Blocks.WEATHERED_COPPER, net.minecraft.block.Blocks.OXIDIZED_COPPER);

        // Cut copper variants
        OXIDATION_MAP.put(net.minecraft.block.Blocks.CUT_COPPER, net.minecraft.block.Blocks.EXPOSED_CUT_COPPER);
        OXIDATION_MAP.put(net.minecraft.block.Blocks.EXPOSED_CUT_COPPER, net.minecraft.block.Blocks.WEATHERED_CUT_COPPER);
        OXIDATION_MAP.put(net.minecraft.block.Blocks.WEATHERED_CUT_COPPER, net.minecraft.block.Blocks.OXIDIZED_CUT_COPPER);
    }

    public PitcherAcidItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        // Vérifier si c'est un bloc Cuivre dans la map d'oxydation
        if (OXIDATION_MAP.containsKey(state.getBlock())) {
            if (!world.isClient) {
                // Obtenir la référence au joueur
                PlayerEntity player = context.getPlayer();
                // Obtenir l'ItemStack de Pitcher Acid
                ItemStack stack = context.getStack();

                // Changer le bloc de cuivre au stade suivant
                Block nextBlock = OXIDATION_MAP.get(state.getBlock());
                world.setBlockState(pos, nextBlock.getDefaultState(), 3);

                // Jouer un petit son si tu veux
                world.playSound(
                        null,
                        pos,
                        SoundEvents.BLOCK_SLIME_BLOCK_BREAK,
                        SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                );

                // Consommer l’acide
                stack.decrement(1);

                // Rendre une bouteille vide
                if (player != null && !player.isCreative()) {
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);

                    // Essayer de l'ajouter dans l'inventaire du joueur
                    if (!player.getInventory().insertStack(emptyBottle)) {
                        // Si l'inventaire est plein, la drop au sol
                        player.dropItem(emptyBottle, false);
                    }
                }
            }
            return ActionResult.success(world.isClient);
        }

        // Sinon, comportement par défaut
        return super.useOnBlock(context);
    }
}
