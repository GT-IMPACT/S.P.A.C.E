package space.impact.space.mixins.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import space.impact.space.utils.world.RenderUtils;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow
    private Minecraft mc;

    @Redirect(
            method = "updateLightmap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/WorldClient;getSunBrightness(F)F",
                    ordinal = 0
            ),
            require = 1
    )
    private float onUpdateLightmap(WorldClient world, float constOne) {
        return RenderUtils.getWorldBrightness(world);
    }

    @Redirect(
            method = "updateFogColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/WorldClient;getSkyColor(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/Vec3;"),
            require = 1)
    private Vec3 onUpdateSkyColor(WorldClient world, Entity entity, float v) {
        return RenderUtils.getSkyColorHook(world);
    }

    @ModifyVariable(
            method = "updateLightmap",
            at = @At(value = "CONSTANT", args = "intValue=255", shift = At.Shift.BEFORE),
            ordinal = 8,
            require = 1)
    private float onUpdateLightmapRed(float value) {
        return RenderUtils.getColorRed(this.mc.theWorld) * value;
    }

    @ModifyVariable(
            method = "updateLightmap",
            at = @At(value = "CONSTANT", args = "intValue=255", shift = At.Shift.BEFORE),
            ordinal = 9,
            require = 1)
    private float onUpdateLightmapGreen(float value) {
        return RenderUtils.getColorGreen(this.mc.theWorld) * value;
    }

    @ModifyVariable(
            method = "updateLightmap",
            at = @At(value = "CONSTANT", args = "intValue=255", shift = At.Shift.BEFORE),
            ordinal = 10,
            require = 1)
    private float onUpdateLightmapBlue(float value) {
        return RenderUtils.getColorBlue(this.mc.theWorld) * value;
    }
}
