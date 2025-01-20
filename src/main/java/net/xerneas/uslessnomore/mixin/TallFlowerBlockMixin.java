package net.xerneas.uslessnomore.mixin;

import net.minecraft.block.*;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.state.StateManager;
import net.xerneas.uslessnomore.UselessNoMore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.ToIntFunction;

@Mixin(TallPlantBlock.class)
public abstract class TallPlantBlockMixin extends PlantBlock {

    protected TallPlantBlockMixin(Settings settings) {
        super(settings.luminance(getLuminance()));
    }

    @Inject(
            method = "<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",
            at = @At("TAIL")
    )
    private void initDefault(Settings settings, CallbackInfo ci){
        setDefaultState(getDefaultState().with(UselessNoMore.LIT_FLOWER, false));
    }

    @ModifyArg(
            method = "<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/PlantBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"
            ),
            index = 0
    )
    private static Settings modifySettings(Settings originalSettings) {
        return originalSettings.luminance(getLuminance());
    }

    @Unique
    private static ToIntFunction<BlockState> getLuminance() {
        return state -> state.get(UselessNoMore.LIT_FLOWER) ? 15 : 0;
    }

    @Inject(
            method = "appendProperties",
            at = @At("TAIL")
    )
    private void appendLITProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(UselessNoMore.LIT_FLOWER);
    }

}
