package net.xerneas.uslessnomore.mixin;

import net.minecraft.block.*;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.xerneas.uslessnomore.UselessNoMore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.ToIntFunction;

@Mixin(PlantBlock.class)
public abstract class PlantBlockMixin extends Block {

    @Unique private static final BooleanProperty LIT = UselessNoMore.LIT_FLOWER;

    protected PlantBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",
            at = @At("TAIL")
    )
    private void initDefault(Settings settings, CallbackInfo ci){
        setDefaultState(getDefaultState().with(LIT, false));

    }

    @ModifyArg(
            method = "<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"
            ),
            index = 0
    )
    private static AbstractBlock.Settings modifySettings(AbstractBlock.Settings originalSettings) {
        return originalSettings.luminance(getLuminance());
    }

    @Unique
    private static ToIntFunction<BlockState> getLuminance() {
        return state -> state.contains(LIT) && state.get(LIT) ? 15 : 0;
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

}
