package space.impact.space.api.events

import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import space.impact.space.api.world.atmosphere.ILightning

object LightningStormHandler {

    fun EntityPlayer.spawnLightning() {
        val world: World = worldObj
        val provider = world.provider

        if (provider is ILightning) {
            val freq = provider.getLightningStormFrequency()
            if (freq > 0.0) {
                val f = freq * 100.0
                if (world.rand.nextInt(f.toInt()) == 0) {
                    createLightning(world)
                }
                if (world.rand.nextInt(f.toInt() * 3) == 0) {
                    createLightning(world)
                }
            }
        }
    }

    private fun EntityPlayer.createLightning(world: World) {
        val closest = world.getClosestPlayerToEntity(this, 100.0)
        if (closest == null || closest.entityId <= this.entityId) {
            val x = (this.posX + this.worldObj.rand.nextInt(64).toDouble() - 32.0).toInt()
            val z = (this.posZ + this.worldObj.rand.nextInt(64).toDouble() - 32.0).toInt()
            val y = (world.getTopSolidOrLiquidBlock(x, z) + (world.provider as ILightning).getYPosLightning()).toDouble()
            val lightning = EntityLightningBolt(world, x.toDouble(), y, z.toDouble())
            world.spawnEntityInWorld(lightning)
        }
    }
}