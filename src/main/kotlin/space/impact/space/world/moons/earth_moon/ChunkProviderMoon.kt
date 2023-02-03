package space.impact.space.world.moons.earth_moon

import net.minecraft.world.World
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.chunk.ChunkProviderBase

class ChunkProviderMoon(
    override val world: World,
    private val seed: Long,
    private val isMapFeature: Boolean,
) : ChunkProviderBase(world, seed, isMapFeature) {

    init {
        biomesList += MoonBiome()
        biomesList += MoonRavine()
    }

    override fun getBiomeGenerator(): BiomeDecoratorSpaceBase {
        return BiomeDecoratorMoonIce()
    }
}