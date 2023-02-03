package space.impact.space.world.moons.earth_moon

import net.minecraft.world.biome.BiomeGenBase
import space.impact.space.api.world.gen.biome.Biome
import space.impact.space.api.world.gen.biome.SpaceBiomes
import space.impact.space.api.world.gen.chunk.SpaceWorldChunkManagerBase

class WorldChunkManagerMoon : SpaceWorldChunkManagerBase() {

    override fun getBiome(): BiomeGenBase {
        return Biome(5555)
    }
}