package net.xerneas.uslessnomore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.xerneas.uslessnomore.block.ModBlocks;
import net.xerneas.uslessnomore.client.GuardianArrowModel;
import net.xerneas.uslessnomore.entities.ModEntities;

public class UselessNoMoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.GUARDIAN_ARROW, GuardianArrowModel::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIT_TORCHFLOWER_BLOCK, RenderLayer.getCutout());
    }
}
