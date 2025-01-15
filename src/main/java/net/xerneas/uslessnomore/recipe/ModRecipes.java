package net.xerneas.uslessnomore.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.xerneas.uslessnomore.UselessNoMore;

public class ModRecipes {
    public static final RecipeType<OxidationRecipe> OXIDATION_TYPE = new RecipeType<>() {};
    public static final RecipeSerializer<OxidationRecipe> OXIDATION_SERIALIZER = new SpecialRecipeSerializer<>(OxidationRecipe::new);

    public static void registerMosRecipes() {
        UselessNoMore.LOGGER.info("Registering Mod Recipes for " + UselessNoMore.MOD_ID);

        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(UselessNoMore.MOD_ID, "oxidation"), OXIDATION_SERIALIZER);
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(UselessNoMore.MOD_ID, "oxidation"), OXIDATION_TYPE);
    }
}
