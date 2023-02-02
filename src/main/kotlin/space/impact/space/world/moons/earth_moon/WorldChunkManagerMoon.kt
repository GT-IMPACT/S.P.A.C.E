package space.impact.space.world.moons.earth_moon

import net.minecraft.world.biome.BiomeGenBase
import space.impact.space.api.world.SpaceWorldChunkManagerBase

class WorldChunkManagerMoon : SpaceWorldChunkManagerBase() {

    override fun getBiome(): BiomeGenBase {
        return BiomeGenBaseMoon.moonFlat
    }
}