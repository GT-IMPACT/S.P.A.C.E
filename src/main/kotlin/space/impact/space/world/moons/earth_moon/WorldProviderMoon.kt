package space.impact.space.world.moons.earth_moon

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.MathHelper
import net.minecraft.world.biome.WorldChunkManager
import net.minecraft.world.chunk.IChunkProvider
import space.impact.space.api.world.ChunkProviderBase
import space.impact.space.api.world.SpaceWorldProviderBase
import space.impact.space.api.world.bodies.CelestialBody
import space.impact.space.api.world.space.Galaxies
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.utils.ext.toVector

class WorldProviderMoon : SpaceWorldProviderBase() {

    override fun getCelestialBody(): CelestialBody = Galaxies.EARTH_MOON

    override fun getChunkProviderClass(): Class<out IChunkProvider> = ChunkProviderMoon::class.java

    override fun getWorldChunkManagerClass(): Class<out WorldChunkManager> = WorldChunkManagerMoon::class.java

    override fun canRainOrSnow(): Boolean = false

    override fun getFogColor(): Vector3 = 0.toVector()

    override fun getSkyColor(): Vector3 = 0.toVector()

    override fun hasSunset(): Boolean = false

    override fun getDayLength(): Long = 192000L

    override fun getGravitationMultiply(): Float = 0.062f

    override fun getSoundVolReductionAmount(): Float = 20.0f

    override fun getThermalLevelModifier(): Float = 0f

    override fun getWindLevel(): Float = 0f

    override fun hasBreathableAtmosphere(): Boolean = false

    @SideOnly(Side.CLIENT)
    override fun getStarBrightness(particalTicks: Float): Float {
        val angle = worldObj.getCelestialAngle(particalTicks)
        var total = 1.0f - (MathHelper.cos(angle * Math.PI.toFloat() * 2.0f) * 2.0f + 0.25f)
        if (total < 0.0f) {
            total = 0.0f
        }
        if (total > 1.0f) {
            total = 1.0f
        }
        return total * total * 0.5f + 0.3f
    }

    override fun isSkyColored(): Boolean {
        return false
    }

    override fun getHorizon(): Double {
        return 44.0
    }

    override fun getAverageGroundLevel(): Int {
        return 68
    }

    override fun canCoordinateBeSpawn(x: Int, z: Int): Boolean {
        return true
    }
}