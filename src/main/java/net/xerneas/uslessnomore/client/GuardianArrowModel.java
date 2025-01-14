package net.xerneas.uslessnomore.client;

import net.xerneas.uslessnomore.UselessNoMore;
import net.xerneas.uslessnomore.entities.GuardianArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GuardianArrowModel  extends ProjectileEntityRenderer<GuardianArrowEntity> {
    public static final Identifier TEXTURE = Identifier.of(UselessNoMore.MOD_ID+":textures/entity/guardian_arrow.png");

    public GuardianArrowModel(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(GuardianArrowEntity entity) {
        return TEXTURE;
    }
}
