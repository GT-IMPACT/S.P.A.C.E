package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import net.minecraft.world.biome.BiomeGenBase
import space.impact.space.api.world.gen.biome.SpaceBiomes
import space.impact.space.api.world.gen.chunk.SpaceWorldChunkManagerBase

class WorldChunkManagerEuropa : SpaceWorldChunkManagerBase() {
    override fun getBiome(): BiomeGenBase {
        return SpaceBiomes.SPACE.setBiomeName("Europa")
    }
}