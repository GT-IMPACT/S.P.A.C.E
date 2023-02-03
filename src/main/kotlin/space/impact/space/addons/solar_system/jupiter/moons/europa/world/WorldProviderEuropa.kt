package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import net.minecraft.world.biome.WorldChunkManager
import net.minecraft.world.chunk.IChunkProvider
import space.impact.space.api.world.bodies.CelestialBody
import space.impact.space.api.world.gen.world.SpaceWorldProviderBase
import space.impact.space.api.world.space.Galaxies.EUROPE_MOON
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.utils.ext.toVector

class WorldProviderEuropa : SpaceWorldProviderBase() {

    override fun canRainOrSnow(): Boolean {
        return false
    }

    override fun getFogColor(): Vector3 {
        return 0.toVector()
    }

    override fun getSkyColor(): Vector3 {
        return 0.toVector()
    }

    override fun hasSunset(): Boolean {
        return false
    }

    override fun getDayLength(): Long {
        return 24000L
    }

    override fun getChunkProviderClass(): Class<out IChunkProvider> {
        return ChunkProviderEuropa::class.java
    }

    override fun getWorldChunkManagerClass(): Class<out WorldChunkManager> {
        return WorldChunkManagerEuropa::class.java
    }

    override fun getGravitationMultiply(): Float {
        return 0f
    }

    override fun getSoundVolReductionAmount(): Float {
        return 0f
    }

    override fun getThermalLevelModifier(): Float {
        return 0f
    }

    override fun getWindLevel(): Float {
        return 0f
    }

    override fun getCelestialBody(): CelestialBody {
        return EUROPE_MOON
    }
}