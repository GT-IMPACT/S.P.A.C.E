package space.impact.space.client.effects

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.particle.EntityFX
import space.impact.space.api.world.world_math.Vector3

@SideOnly(Side.CLIENT)
object EffectHandler {

    fun spawnParticle(position: Vector3, motion: Vector3, effectData: EffectData) {
        val mc = FMLClientHandler.instance().client
        if (mc?.renderViewEntity != null && mc.effectRenderer != null) {

            val particle: EntityFX = when (effectData) {
                is EffectData.IceWaterEffect -> EntityIceWater(
                    mc.theWorld, position, motion, effectData.age,
                    effectData.particleId, effectData.isGravity, effectData.color, effectData.size
                )
            }

            particle.prevPosX = particle.posX
            particle.prevPosY = particle.posY
            particle.prevPosZ = particle.posZ
            mc.effectRenderer.addEffect(particle)
        }
    }
}