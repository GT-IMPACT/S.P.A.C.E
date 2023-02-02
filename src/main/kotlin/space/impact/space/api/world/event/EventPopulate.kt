package space.impact.space.api.world.event

import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.world.World
import java.util.*

sealed class EventPopulate(
    val world: World?,
    val rand: Random?,
    val chunkX: Int,
    val chunkZ: Int
) : Event() {
    class Pre(
        world: World?,
        rand: Random?,
        worldX: Int,
        worldZ: Int
    ) : EventPopulate(world, rand, worldX, worldZ)

    class Post(
        world: World?,
        rand: Random?,
        worldX: Int,
        worldZ: Int
    ) : EventPopulate(world, rand, worldX, worldZ)
}