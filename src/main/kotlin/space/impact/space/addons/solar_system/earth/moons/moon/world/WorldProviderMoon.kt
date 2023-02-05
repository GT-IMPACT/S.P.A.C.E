package space.impact.space.addons.solar_system.earth.moons.moon.world

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.MathHelper
import net.minecraft.world.biome.WorldChunkManager
import net.minecraft.world.chunk.IChunkProvider
import net.minecraftforge.client.IRenderHandler
import space.impact.space.api.world.bodies.CelestialBody
import space.impact.space.api.world.gen.world.SpaceWorldProviderBase
import space.impact.space.api.world.render.CloudRenderer
import space.impact.space.api.world.space.Galaxies
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.utils.ext.toVector

class WorldProviderMoon : SpaceWorldProviderBase() {

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
        return 192000L
    }

    override fun getChunkProviderClass(): Class<out IChunkProvider> {
        return ChunkProviderMoon::class.java
    }

    override fun getWorldChunkManagerClass(): Class<out WorldChunkManager> {
        return WorldChunkManagerMoon::class.java
    }

    override fun getGravitationMultiply(): Float {
        return 0.062f
    }

    override fun getSoundVolReductionAmount(): Float {
        return 20.0f
    }

    override fun getThermalLevelModifier(): Float {
        return 0f
    }

    override fun getWindLevel(): Float {
        return 0f
    }

    override fun getCelestialBody(): CelestialBody {
        return Galaxies.EARTH_MOON
    }

    override fun hasBreathableAtmosphere(): Boolean {
        return false
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

    @SideOnly(Side.CLIENT)
    override fun getStarBrightness(par1: Float): Float {
        val var2 = worldObj.getCelestialAngle(par1)
        var var3 = 1.0f - (MathHelper.cos(var2 * Math.PI.toFloat() * 2.0f) * 2.0f + 0.25f)
        if (var3 < 0.0f) {
            var3 = 0.0f
        }
        if (var3 > 1.0f) {
            var3 = 1.0f
        }
        return var3 * var3 * 0.5f + 0.3f
    }

    @SideOnly(Side.CLIENT)
    override fun getSkyRenderer(): IRenderHandler {
        if (super.getSkyRenderer() == null) {
            skyRenderer = SkyProviderMoon()
        }
        return super.getSkyRenderer()
    }

    @SideOnly(Side.CLIENT)
    override fun getCloudRenderer(): IRenderHandler {
        return CloudRenderer()
    }

    override fun getFallDamageModifier(): Float {
        return 0.18f
    }
}