package net.xerneas.uslessnomore.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.TorchflowerBlock;
import net.minecraft.entity.effect.StatusEffects;

public class LitTorchflowerBlock extends FlowerBlock {
    public LitTorchflowerBlock(AbstractBlock.Settings settings) {
        super(
                StatusEffects.NIGHT_VISION,
                5.0F,
                settings
        );
    }
}
