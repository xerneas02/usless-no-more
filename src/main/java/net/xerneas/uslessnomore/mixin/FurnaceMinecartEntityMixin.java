package net.xerneas.uslessnomore.mixin;

// This file is heavily based on the mod Cammie's Minecart Tweaks
// https://github.com/CammiePone/Cammies-Minecart-Tweaks/blob/1.20-dev/src/main/java/dev/cammiescorner/cammiesminecarttweaks/common/compat/MinecartTweaksConfig.java

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.xerneas.uslessnomore.UselessNoMore;
import net.xerneas.uslessnomore.gamerule.ModGamerules;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(FurnaceMinecartEntity.class)
public abstract class FurnaceMinecartEntityMixin extends AbstractMinecartEntity {
    @Shadow protected abstract boolean isLit();
    @Shadow private int fuel;
    @Shadow public double pushX;
    @Shadow public double pushZ;
    @Shadow @Final @Mutable private static Ingredient ACCEPTABLE_FUEL;
    @Shadow public abstract ActionResult interact(PlayerEntity player, Hand hand);

    @Unique private int altFuel;
    @Unique private double altPushX;
    @Unique private double altPushZ;
    @Unique private static final Ingredient OLD_ACCEPTABLE_FUEL = ACCEPTABLE_FUEL;
    @Unique private final Set<AbstractMinecartEntity> train = new HashSet<>();
    @Unique private ChunkPos prevChunkPos;
    private  static  boolean isChunkLoaded = false;
    private static boolean useCampfireSmoke = true;
    private static int furnaceMaxBurnTime = 72000;
    private static double furnaceMinecartSpeed = 7D;
    private static boolean furnacesCanUseAllFuels = true;

    protected FurnaceMinecartEntityMixin(EntityType<?> entityType, World world) { super(entityType, world); }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDD)V", at = @At("TAIL"))
    public void initPrevChunPos(World world, double x, double y, double z, CallbackInfo info) {
        prevChunkPos = getChunkPos();
    }

    @Inject(method = "getMaxSpeed", at = @At("RETURN"), cancellable = true)
    public void increaseSpeed(CallbackInfoReturnable<Double> info) {
        if(this.getWorld() instanceof ServerWorld server) {
            furnaceMinecartSpeed = this.getWorld().getGameRules().getInt(ModGamerules.FURNACE_MINECART_SPEED);
        }
        if(isLit())
            info.setReturnValue(Math.max(0.1, furnaceMinecartSpeed * 0.05)); // (this.isTouchingWater() ? 3.0 : 4.0) / 20.0) Furnace minecart normal speed
        else
            info.setReturnValue(super.getMaxSpeed());
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void loadChunks(CallbackInfo info) {
        if(this.getWorld() instanceof ServerWorld server) {
            boolean furnaceMinecartsLoadChunks = this.getWorld().getGameRules().getBoolean(ModGamerules.DO_FURNACE_MINECART_LOAD_CHUNKS);

            ChunkPos currentChunkPos = ChunkSectionPos.from(this).toChunkPos();

            if(fuel > 0 && furnaceMinecartsLoadChunks) {
                server.getChunkManager().addTicket(ChunkTicketType.PLAYER, currentChunkPos, 3, getChunkPos());
                isChunkLoaded = true;
            }
            if((!currentChunkPos.equals(prevChunkPos) || fuel <= 0)) {
                server.getChunkManager().removeTicket(ChunkTicketType.PLAYER, prevChunkPos, 3, getChunkPos());
            }
            if(isChunkLoaded && !furnaceMinecartsLoadChunks){
                server.getChunkManager().removeTicket(ChunkTicketType.PLAYER, prevChunkPos, 3, getChunkPos());
                server.getChunkManager().removeTicket(ChunkTicketType.PLAYER, currentChunkPos, 3, getChunkPos());
                isChunkLoaded = false;
            }


            prevChunkPos = currentChunkPos;
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);

        if(isChunkLoaded && this.getWorld() instanceof ServerWorld server) {
            server.getChunkManager().removeTicket(ChunkTicketType.PLAYER, prevChunkPos, 3, getChunkPos());
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    public void addOtherFuels(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if(this.getWorld() instanceof ServerWorld server) {
            furnacesCanUseAllFuels = this.getWorld().getGameRules().getBoolean(ModGamerules.DO_FURNACE_MINECART_USE_ALL_FUELS);
            furnaceMaxBurnTime = this.getWorld().getGameRules().getInt(ModGamerules.FURNACE_MINECART_MAX_BURN_TIME);
        }

        if(furnacesCanUseAllFuels) {
            ItemStack stack = player.getStackInHand(hand);
            Map<Item, Integer> fuels = AbstractFurnaceBlockEntity.createFuelTimeMap();

            if(!stack.isEmpty() && fuels.containsKey(stack.getItem())) {
                int fuelTime = fuels.getOrDefault(stack.getItem(), 0);

                if(!player.isCreative() && fuelTime > 0) {
                    if(stack.getItem() instanceof BucketItem)
                        player.getInventory().setStack(player.getInventory().selectedSlot, BucketItem.getEmptiedStack(stack, player));
                    else
                        stack.decrement(1);
                }

                if(stack.getItem() instanceof BucketItem) {
                    SoundEvent soundEvent = SoundEvents.ITEM_BUCKET_EMPTY_LAVA;
                    this.getWorld().playSound(player, player.getBlockPos(), soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }

                fuel = (int) Math.min(furnaceMaxBurnTime, fuel + (fuelTime * 2.25));
            }

            if(fuel > 0) {
                pushX = getX() - player.getX();
                pushZ = getZ() - player.getZ();
            }

            ACCEPTABLE_FUEL = Ingredient.empty();
            info.setReturnValue(ActionResult.success(this.getWorld().isClient));
        }
        else {
            ACCEPTABLE_FUEL = OLD_ACCEPTABLE_FUEL;
        }
    }

    @ModifyConstant(method = "interact", constant = @Constant(intValue = 32000))
    public int maxBurnTime(int maxBurnTime) {
        if(this.getWorld() instanceof ServerWorld server) {
            int furnaceMaxBurnTime = this.getWorld().getGameRules().getInt(ModGamerules.FURNACE_MINECART_MAX_BURN_TIME);
        }
        return furnaceMaxBurnTime;
    }

    @ModifyArgs(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
    ))
    public void changeSmokeParticle(Args args) {
        if(this.getWorld() instanceof ServerWorld server) {
            useCampfireSmoke = this.getWorld().getGameRules().getBoolean(ModGamerules.DO_FURNACE_MINECART_USE_CAMPFIRE_PARTICLES);
        }

        if (useCampfireSmoke) {
            args.set(0, ParticleTypes.CAMPFIRE_COSY_SMOKE);
        } else {
            args.set(0, ParticleTypes.LARGE_SMOKE);
        }

        args.set(1, getX() + (random.nextFloat() - 0.5));
        args.set(2, getY() + 1);
        args.set(3, getZ() + (random.nextFloat() - 0.5));
        args.set(5, 0.2);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"
    ))
    public int removeRandom(int i) {
        return 1;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readNbt(NbtCompound nbt, CallbackInfo info) {
        fuel = nbt.getInt("RealFuel");
        altFuel = nbt.getInt("AltFuel");
        altPushX = nbt.getDouble("AltPushX");
        altPushZ = nbt.getDouble("AltPushZ");
        prevChunkPos = new ChunkPos(nbt.getLong("PrevChunkPos"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeNbt(NbtCompound nbt, CallbackInfo info) {
        if(fuel > Short.MAX_VALUE)
            nbt.putShort("Fuel", Short.MAX_VALUE);

        nbt.putInt("RealFuel", fuel);
        nbt.putInt("AltFuel", altFuel);
        nbt.putDouble("AltPushX", altPushX);
        nbt.putDouble("AltPushZ", altPushZ);
        nbt.putLong("PrevChunkPos", prevChunkPos.toLong());
    }
}
