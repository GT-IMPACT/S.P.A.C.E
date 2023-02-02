package space.impact.space.world.moons.earth_moon

import net.minecraft.world.World
import space.impact.space.api.world.ChunkProviderBase

class ChunkProviderMoon(
    override val world: World,
    private val seed: Long,
    private val isMapFeature: Boolean,
) : ChunkProviderBase(world, seed, isMapFeature) {



}