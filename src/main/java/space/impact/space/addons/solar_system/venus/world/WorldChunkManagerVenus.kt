package space.impact.space.addons.solar_system.venus.world

import net.minecraft.world.biome.BiomeGenBase
import space.impact.space.api.world.gen.biome.SpaceBiomeGenBase
import space.impact.space.api.world.gen.chunk.SpaceWorldChunkManagerBase

class WorldChunkManagerVenus : SpaceWorldChunkManagerBase() {

    init {
        biomesToSpawnIn += SpaceBiomeGenBase.SPACE_LOW_PLAINS.setBiomeName("VenusLowPlains")
        biomesToSpawnIn += SpaceBiomeGenBase.SPACE_SHALLOW_WATERS.setBiomeName("VenusShallowWaters")
    }

    override fun getBiome(): BiomeGenBase {
        return SpaceBiomeGenBase.SPACE_LOW_HILLS.setBiomeName("VenusLowHills")
    }
}