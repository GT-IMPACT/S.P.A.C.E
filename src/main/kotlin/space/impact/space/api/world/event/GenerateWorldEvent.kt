package space.impact.space.api.world.event

import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.world.World
import java.util.*

sealed class GenerateWorldEvent(
    val world: World?,
    val random: Random?,
    val chunkX: Int,
    val chunkZ: Int
) : Event() {

    class Pre(
        world: World?,
        random: Random?,
        chunkX: Int,
        chunkZ: Int
    ) : GenerateWorldEvent(world, random, chunkX, chunkZ)

    class Post(
        world: World?,
        random: Random?,
        chunkX: Int,
        chunkZ: Int
    ) : GenerateWorldEvent(world, random, chunkX, chunkZ)
}