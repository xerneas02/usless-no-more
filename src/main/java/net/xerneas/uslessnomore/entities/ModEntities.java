package net.xerneas.uslessnomore.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.xerneas.uslessnomore.UselessNoMore;


public class ModEntities {

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(UselessNoMore.MOD_ID, id), type.build());
    }

    public static final EntityType<GuardianArrowEntity> GUARDIAN_ARROW = register(
            "guardian_arrow",
            EntityType.Builder.<GuardianArrowEntity>create(GuardianArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
    );

    public static void registerModEntities() {
        UselessNoMore.LOGGER.info("Registering Mod Entities for " + UselessNoMore.MOD_ID);
    }
}