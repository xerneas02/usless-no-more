package net.xerneas.uslessnomore.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

public class OxidationRecipeSerializer implements RecipeSerializer<OxidationRecipe> {
    private final MapCodec<OxidationRecipe> codec;
    private final PacketCodec<RegistryByteBuf, OxidationRecipe> packetCodec;

    public OxidationRecipeSerializer() {
        this.codec = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        CraftingRecipeCategory.CODEC
                                .fieldOf("category")
                                .orElse(CraftingRecipeCategory.MISC)
                                .forGetter(OxidationRecipe::getCategory)
                ).apply(instance, OxidationRecipe::new)
        );

        this.packetCodec = PacketCodec.tuple(
                CraftingRecipeCategory.PACKET_CODEC,
                OxidationRecipe::getCategory,
                OxidationRecipe::new
        );
    }

    @Override
    public MapCodec<OxidationRecipe> codec() {
        return this.codec;
    }

    @Override
    public PacketCodec<RegistryByteBuf, OxidationRecipe> packetCodec() {
        return this.packetCodec;
    }
}
