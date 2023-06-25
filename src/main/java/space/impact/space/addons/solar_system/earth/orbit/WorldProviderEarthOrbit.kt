package space.impact.space.addons.solar_system.earth.orbit

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IRenderHandler
import space.impact.space.MODID
import space.impact.space.api.world.gen.world.SpaceWorldProviderBaseProvider
import space.impact.space.api.world.render.CloudRenderer
import space.impact.space.api.world.space.Galaxies.EARTH_ORBIT
import space.impact.space.api.world.space.IOrbitProvider
import space.impact.space.api.world.space.IZeroGravitationProvider
import space.impact.space.utils.ext.toVector

class WorldProviderEarthOrbit : SpaceWorldProviderBaseProvider(), IOrbitProvider, IZeroGravitationProvider {

    override fun canRainOrSnow() = false
    override fun getFogColor() = 0.toVector()
    override fun getSkyColor() = 0.toVector()
    override fun hasSunset() = false
    override fun getDayLength() = 24000L
    override fun getChunkProviderClass() = ChunkProviderOrbit::class.java
    override fun getWorldChunkManagerClass() = WorldChunkManagerOrbit::class.java
    override fun getGravitationMultiply() = 0.074F
    override fun getSoundVolReductionAmount() = Float.MAX_VALUE
    override fun getThermalLevelModifier() = -270f
    override fun getWindLevel() = 0f
    override fun getCelestialBody() = EARTH_ORBIT
    override fun isSkyColored() = true
    override fun getHorizon() = 44.0
    override fun hasBreathableAtmosphere() = false
    override fun getYCoordToTeleportToPlanet() = 20
    override fun getDimensionIdToTarget() = 0 //EARTH ID

    override fun isDaytime() =
        worldObj.getCelestialAngle(0F).let { it < 0.42F || it > 0.58F }

    @SideOnly(Side.CLIENT)
    override fun getStarBrightness(par1: Float): Float {
        val angle = worldObj.getCelestialAngle(par1)
        var piAngle: Float = 1.0f - (MathHelper.cos(angle * Math.PI.toFloat() * 2.0f) * 2.0f + 0.25f)
        if (piAngle < 0.0f) {
            piAngle = 0.0f
        }
        if (piAngle > 1.0f) {
            piAngle = 1.0f
        }
        return piAngle * piAngle * 0.5f + 0.3f
    }

    @SideOnly(Side.CLIENT)
    override fun getSunBrightness(par1: Float): Float {
        val angle = worldObj.getCelestialAngle(1.0f)
        var piAngle = 1.25f - (MathHelper.cos(angle * Math.PI.toFloat() * 2.0f) * 2.0f + 0.2f)
        if (piAngle < 0.0f) {
            piAngle = 0.0f
        }
        if (piAngle > 1.0f) {
            piAngle = 1.0f
        }
        piAngle = 1.2f - piAngle
        return piAngle * 0.25f
    }

    @SideOnly(Side.CLIENT)
    override fun getSkyRenderer(): IRenderHandler {
        if (super.getSkyRenderer() == null) {
            skyRenderer = SkyProviderOrbit(
                renderMoon = true,
                renderSun = true,
            )
        }
        return super.getSkyRenderer()
    }

    @SideOnly(Side.CLIENT)
    override fun getCloudRenderer(): IRenderHandler {
        return CloudRenderer()
    }
}
