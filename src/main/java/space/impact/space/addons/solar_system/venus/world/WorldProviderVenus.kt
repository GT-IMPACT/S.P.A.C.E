package space.impact.space.addons.solar_system.venus.world

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.MathHelper
import net.minecraft.world.biome.WorldChunkManager
import net.minecraft.world.chunk.IChunkProvider
import net.minecraftforge.client.IRenderHandler
import space.impact.space.api.world.atmosphere.ILightning
import space.impact.space.api.world.atmosphere.PlanetFog
import space.impact.space.api.world.bodies.CelestialBody
import space.impact.space.api.world.gen.world.SpaceWorldProviderBase
import space.impact.space.api.world.space.Galaxies
import space.impact.space.api.world.world_math.Vector3

class WorldProviderVenus : SpaceWorldProviderBase(), ILightning, PlanetFog {

    private var raining = false
    private var rainTime = 100
    private var rainChange = 100
    private var targetRain = 0.0f

    override fun canRainOrSnow(): Boolean {
        return true
    }

    @SideOnly(Side.CLIENT)
    override fun getFogColor(): Vector3 {
        val night = getStarBrightness(1.0f)
        val day = 1.0f - getStarBrightness(1.0f)
        val dayColR = 203.0f / 255.0f
        val dayColG = 147.0f / 255.0f
        val dayColB = 0.0f / 255.0f
        val nightColR = 1.0f / 255.0f
        val nightColG = 1.0f / 255.0f
        val nightColB = 1.0f / 255.0f
        return Vector3(
            dayColR * day + nightColR * night,
            dayColG * day + nightColG * night,
            dayColB * day + nightColB * night
        )
    }

    @SideOnly(Side.CLIENT)
    override fun getSkyColor(): Vector3 {
        val night = getStarBrightness(1.0f)
        val day = 1.0f - getStarBrightness(1.0f)
        val dayColR = 105.0f / 255.0f
        val dayColG = 107.0f / 255.0f
        val dayColB = 281.0f / 255.0f
        val nightColR = 2.0f / 255.0f
        val nightColG = 2.0f / 255.0f
        val nightColB = 2.0f / 255.0f
        return Vector3(
            dayColR * day + nightColR * night,
            dayColG * day + nightColG * night,
            dayColB * day + nightColB * night
        )
    }

    override fun canBlockFreeze(x: Int, y: Int, z: Int, byWater: Boolean): Boolean {
        return false
    }

    override fun isSkyColored(): Boolean {
        return true
    }

    override fun hasSunset(): Boolean {
        return false
    }

    override fun getDayLength(): Long {
        return 182000L
    }

    override fun getChunkProviderClass(): Class<out IChunkProvider> {
        return ChunkProviderVenus::class.java
    }


    override fun getWorldChunkManagerClass(): Class<out WorldChunkManager> {
        return WorldChunkManagerVenus::class.java
    }

    override fun getGravitationMultiply(): Float {
        return 0.057f
    }

    override fun getSoundVolReductionAmount(): Float {
        return Float.MAX_VALUE
    }

    override fun getThermalLevelModifier(): Float {
        return 5.0f
    }

    override fun getWindLevel(): Float {
        return 0f
    }

    override fun getHorizon(): Double {
        return 44.0
    }

    override fun hasBreathableAtmosphere(): Boolean {
        return false
    }

    override fun getFogDensity(x: Int, y: Int, z: Int): Float {
        return 0.25f
    }

    override fun getFogColor(x: Int, y: Int, z: Int): Int {
        return 0xC2982C - 16777216
    }

    override fun getCelestialBody(): CelestialBody {
        return Galaxies.VENUS_PLANET
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
    override fun getSunBrightness(par1: Float): Float {
        val f1 = worldObj.getCelestialAngle(1.0f)
        var f2 = 1.25f - (MathHelper.cos(f1 * Math.PI.toFloat() * 2.0f) * 2.0f + 0.2f)
        if (f2 < 0.0f) {
            f2 = 0.0f
        }
        if (f2 > 1.0f) {
            f2 = 1.0f
        }
        f2 = 1.2f - f2
        return f2 * 1.0f
    }

    private val cloud = CloudProviderVenus()
    private val sky = CloudProviderVenus()

    @SideOnly(Side.CLIENT)
    override fun getCloudRenderer(): IRenderHandler {
        return cloud
    }

    @SideOnly(Side.CLIENT)
    override fun getSkyRenderer(): IRenderHandler {
        return sky
    }

    @SideOnly(Side.CLIENT)
    override fun getWeatherRenderer(): IRenderHandler? {
        if (super.getWeatherRenderer() == null) {
            weatherRenderer = WeatherProviderVenus()
        }
        return super.getWeatherRenderer()
    }

    override fun getLightningStormFrequency(): Double {
        return 0.8
    }

    override fun getYPosLightning(): Int {
        return 20
    }

    override fun getFallDamageModifier(): Float {
        return 0.8f
    }

    override fun updateWeather() {
//        super.updateWeather()
        if (!worldObj.isRemote) {
            if (--this.rainTime <= 0) {
                this.raining = !this.raining
                if (this.raining) {
                    this.rainTime = worldObj.rand.nextInt(3600) + 1000
                } else {
                    this.rainTime = worldObj.rand.nextInt(2000) + 1000
                }
            }
            if (--this.rainChange <= 0) {
                this.targetRain = 0.15f + worldObj.rand.nextFloat() * 0.45f
                this.rainChange = worldObj.rand.nextInt(200) + 100
            }
            var strength = worldObj.rainingStrength
            worldObj.prevRainingStrength = strength
            if (this.raining && strength < this.targetRain) {
                strength += 0.004f
            } else if (!this.raining || strength > this.targetRain) {
                strength -= 0.004f
            }
            worldObj.rainingStrength = MathHelper.clamp_float(strength, 0.0f, 0.6f)
        }
    }
}