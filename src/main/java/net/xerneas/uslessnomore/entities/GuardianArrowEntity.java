package net.xerneas.uslessnomore.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.xerneas.uslessnomore.UselessNoMore;
import net.xerneas.uslessnomore.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class GuardianArrowEntity extends PersistentProjectileEntity {

    public GuardianArrowEntity(EntityType<? extends GuardianArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public GuardianArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntities.GUARDIAN_ARROW, owner, world, stack, shotFrom);
    }

    public GuardianArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntities.GUARDIAN_ARROW, x, y, z, world, stack, shotFrom);
    }

    @Override
    protected float getDragInWater() {
        return 1.0F;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.GUARDIAN_ARROW);
    }
}
