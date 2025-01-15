package net.xerneas.uslessnomore.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.xerneas.uslessnomore.UselessNoMore;

public class ModBlocks {

    public static final Block LIT_TORCHFLOWER_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of(UselessNoMore.MOD_ID, "lit_torchflower"),
            new LitTorchflowerBlock(
                    AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .luminance(state -> 15)
            )
    );


    public static void registerModBlocks() {
        UselessNoMore.LOGGER.info("Registering Mod Blocks for " + UselessNoMore.MOD_ID);
    }
}
