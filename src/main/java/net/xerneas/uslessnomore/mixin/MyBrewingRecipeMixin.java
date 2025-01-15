package net.xerneas.uslessnomore.mixin;

import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.BrewingRecipeRegistry.Builder;
import net.xerneas.uslessnomore.potion.ModPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class MyBrewingRecipeMixin {

    /**
     * Injects after the vanilla default recipes are registered.
     * This method signature matches "registerDefaults(Lnet/minecraft/recipe/BrewingRecipeRegistry$Builder;)V"
     */
    @Inject(
            method = "registerDefaults(Lnet/minecraft/recipe/BrewingRecipeRegistry$Builder;)V",
            at = @At("TAIL")
    )
    private static void registerMyModPotions(Builder builder, CallbackInfo ci) {
/*
        builder.registerPotionRecipe(
                Potions.AWKWARD,            // Base potion
                Items.PITCHER_PLANT,      // Reagent item
                ModPotions.PITCHER_ACID_POTION   // Output custom potion
        );*/

    }
}
