package space.impact.space.api.world.space

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.WorldServer

interface ISpaceBehaviorPlayersProvider {

    companion object {
        private const val TICK_UPDATE = 20
        fun handleSpaceBehaviorPlayersProvider(world: WorldServer, provider: ISpaceBehaviorPlayersProvider) {
            if (world.totalWorldTime % TICK_UPDATE == 0L) {
                for (entity in world.playerEntities.toTypedArray()) {
                    if (entity is EntityPlayer) {
                        provider.playerBehavior(entity)
                    }
                }
            }
        }
    }

    fun playerBehavior(player: EntityPlayer)
}

