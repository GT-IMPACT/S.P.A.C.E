package space.impact.space.addons.solar_system.earth.orbit

import space.impact.space.api.world.gen.chunk.SpaceWorldChunkManagerBase

class WorldChunkManagerOrbit : SpaceWorldChunkManagerBase() {
    override fun getBiome() = BiomeGenBaseOrbit.space
}