package space.impact.space.api.world.space

import net.minecraft.entity.Entity
import net.minecraft.world.WorldServer
import space.impact.space.utils.world.WorldUtils

interface IOrbitProvider {

    companion object {
        fun handleOrbitProvider(world: WorldServer, provider: IOrbitProvider) {
            for (entity in world.loadedEntityList.toTypedArray()) {
                if (entity is Entity) {
                    if (!entity.isDead && entity.posY <= provider.getYCoordToTeleportToPlanet()) {
                        WorldUtils.transferEntityToDimension(entity, provider.getDimensionIdToTarget(), world, true)
                    }
                }
            }
        }
    }

    fun getYCoordToTeleportToPlanet(): Int
    fun getDimensionIdToTarget(): Int
}

