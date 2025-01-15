package net.xerneas.uslessnomore.recipe;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.xerneas.uslessnomore.UselessNoMore;
import net.xerneas.uslessnomore.item.PitcherAcidItem;

import java.util.Map;

public class OxidationRecipe extends SpecialCraftingRecipe {

    public OxidationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world)  {
        boolean hasAcid = false;
        boolean hasOxidizableBlock = false;

        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof PitcherAcidItem) {
                    if (hasAcid) return false; // Only one acid allowed
                    hasAcid = true;
                } else if (PitcherAcidItem.OXIDATION_MAP.containsKey(Registries.BLOCK.get(Registries.ITEM.getId(stack.getItem())))) {
                    if (hasOxidizableBlock) return false; // Only one block allowed
                    hasOxidizableBlock = true;
                } else {
                    return false; // Invalid item
                }
            }
        }

        return hasAcid && hasOxidizableBlock;
    }



    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);

            if (!stack.isEmpty() && !(stack.getItem() instanceof PitcherAcidItem)) {
                Block block = Registries.BLOCK.get(Registries.ITEM.getId(stack.getItem()));
                Block oxidizedBlock = PitcherAcidItem.OXIDATION_MAP.get(block);

                if (oxidizedBlock != null) {
                    return new ItemStack(oxidizedBlock.asItem());
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.OXIDATION_SERIALIZER;
    }
}
