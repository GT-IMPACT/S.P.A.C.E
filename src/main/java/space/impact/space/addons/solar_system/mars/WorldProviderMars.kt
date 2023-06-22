package space.impact.space.addons.solar_system.mars

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.MathHelper
import net.minecraft.world.biome.WorldChunkManager
import net.minecraft.world.chunk.IChunkProvider
import net.minecraftforge.client.IRenderHandler
import space.impact.space.addons.solar_system.jupiter.moons.europa.world.SkyProviderEuropa
import space.impact.space.api.world.bodies.CelestialBody
import space.impact.space.api.world.gen.world.SpaceWorldProviderBase
import space.impact.space.api.world.render.CloudRenderer
import space.impact.space.api.world.space.Galaxies
import space.impact.space.api.world.world_math.Vector3

class WorldProviderMars : SpaceWorldProviderBase() {

    override fun getFogColor(): Vector3 {
        val f = 1.0f - getStarBrightness(1.0f)
        return Vector3(210f / 255f * f, 120f / 255f * f, 59f / 255f * f)
    }

    override fun getSkyColor(): Vector3 {
        val f = 1.0f - getStarBrightness(1.0f)
        return Vector3(154 / 255.0f * f, 114 / 255.0f * f, 66 / 255.0f * f)
    }

    override fun canRainOrSnow(): Boolean {
        return false
    }

    override fun hasSunset(): Boolean {
        return false
    }

    override fun getDayLength(): Long {
        return 24660L
    }

    override fun getChunkProviderClass(): Class<out IChunkProvider>? {
        return ChunkProviderMars::class.java
    }

    override fun getWorldChunkManagerClass(): Class<out WorldChunkManager>? {
        return WorldChunkManagerMars::class.java
    }

    @SideOnly(Side.CLIENT)
    override fun getStarBrightness(par1: Float): Float {
        val f1 = worldObj.getCelestialAngle(par1)
        var f2 = 1.0f - (MathHelper.cos(f1 * Math.PI.toFloat() * 2.0f) * 2.0f + 0.25f)
        if (f2 < 0.0f) {
            f2 = 0.0f
        }
        if (f2 > 1.0f) {
            f2 = 1.0f
        }
        return f2 * f2 * 0.75f
    }

    override fun getHorizon(): Double {
        return 44.0
    }

    override fun getAverageGroundLevel(): Int {
        return 76
    }

    override fun getGravitationMultiply(): Float {
        return 0.057f
    }

    override fun getSoundVolReductionAmount(): Float {
        return 10.0f
    }

    override fun getCelestialBody(): CelestialBody{
        return Galaxies.MARS_PLANET
    }

    override fun hasBreathableAtmosphere(): Boolean {
        return false
    }

    override fun getThermalLevelModifier(): Float {
        return (-1).toFloat()
    }

    override fun getWindLevel(): Float {
        return 0.3f
    }

    @SideOnly(Side.CLIENT)
    override fun getCloudRenderer(): IRenderHandler {
        return CloudRenderer()
    }

    @SideOnly(Side.CLIENT)
    override fun getSkyRenderer(): IRenderHandler? {
        if (super.getSkyRenderer() == null) {
            skyRenderer = SkyProviderMars(this)
        }
        return super.getSkyRenderer()
    }
}