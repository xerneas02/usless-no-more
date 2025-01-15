package net.xerneas.uslessnomore.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.TorchflowerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class LitTorchflowerBlock extends FlowerBlock {
    public LitTorchflowerBlock(AbstractBlock.Settings settings) {
        super(
                StatusEffects.NIGHT_VISION,
                5.0F,
                settings
        );
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && !(entity instanceof ItemEntity) && !(entity instanceof BeeEntity)) {
            entity.setOnFireFor(2);
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        double x = (double)pos.getX() + 0.5 + (random.nextDouble() * 0.3);
        double y = (double)pos.getY() + 0.7 + (random.nextDouble() * 0.3);
        double z = (double)pos.getZ() + 0.5 + (random.nextDouble() * 0.3);

        if (random.nextInt(5) == 0) {
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }
}
