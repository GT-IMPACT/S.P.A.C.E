package space.impact.space.api.world.gen.world

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import net.minecraft.village.VillageCollection
import net.minecraft.world.World
import net.minecraft.world.WorldProvider
import net.minecraft.world.biome.WorldChunkManager
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.IChunkProvider
import net.minecraftforge.client.IRenderHandler
import space.impact.space.api.world.atmosphere.Atmospheric
import space.impact.space.api.world.atmosphere.AtmosphericGas
import space.impact.space.api.world.render.CloudRenderer
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.config.Config
import java.lang.reflect.Field
import java.util.*

abstract class SpaceWorldProviderBase : WorldProvider(), SpaceProvider {

    companion object {
        var tickCounter: Field? = null

        init {
            try {
                tickCounter = VillageCollection::class.java
                    .getDeclaredField("tickCounter")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var timeCurrentOffset = 0L
    var preTickTime = Long.MIN_VALUE
    private var saveTCO = 0L

    override fun updateWeather() {
        if (!worldObj.isRemote) {
            val newTime = worldObj.worldInfo.worldTime
            if (preTickTime == Long.MIN_VALUE) {
                var savedTick = 0
                try {
                    tickCounter?.isAccessible = true
                    savedTick = tickCounter?.getInt(worldObj.villageCollectionObj) ?: 0
                    if (savedTick < 0) {
                        savedTick = 0
                    }
                } catch (_: Exception) {}
                timeCurrentOffset = savedTick - newTime
            } else {
                val diff = newTime - preTickTime
                if (diff > 1L) {
                    timeCurrentOffset -= diff - 1L
                    this.saveTime()
                }
            }
            preTickTime = newTime
            saveTCO = 0L
        }
        if (this.canRainOrSnow()) {
            super.updateWeather()
        } else {
            worldObj.worldInfo.rainTime = 0
            worldObj.worldInfo.isRaining = false
            worldObj.worldInfo.thunderTime = 0
            worldObj.worldInfo.isThundering = false
            worldObj.rainingStrength = 0.0f
            worldObj.thunderingStrength = 0.0f
        }
    }

    /**
     * Weather or not there will be rain or snow in this dim
     */
    abstract fun canRainOrSnow(): Boolean

    /**
     * The fog color in this dimension
     */
    abstract fun getFogColor(): Vector3?

    /**
     * The sky color in this dimension
     */
    abstract fun getSkyColor(): Vector3?

    /**
     * Whether or not to render vanilla sunset (can be overridden with custom sky provider)
     */
    abstract fun hasSunset(): Boolean

    /**
     * The length of day in this dimension
     * Default: 24000
     */
    abstract fun getDayLength(): Long

    abstract fun getChunkProviderClass(): Class<out IChunkProvider>?

    abstract fun getWorldChunkManagerClass(): Class<out WorldChunkManager>?

    override fun createChunkGenerator(): IChunkProvider? {
        try {
            val chunkProviderClass = getChunkProviderClass() ?: return null
            val constructors = chunkProviderClass.constructors
            for (constructor in constructors) {
                val types = arrayOf<Any>(World::class.java, Long::class.java, Boolean::class.java)
                if (Arrays.equals(constructor.parameterTypes, types)) {
                    return constructor.newInstance(worldObj, worldObj.seed, worldObj.worldInfo.isMapFeaturesEnabled) as IChunkProvider
                } else if (constructor.parameterTypes.isEmpty()) {
                    return constructor.newInstance() as IChunkProvider
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun registerWorldChunkManager() {
        try {
            val chunkManagerClass = getWorldChunkManagerClass() ?: return super.registerWorldChunkManager()
            val constructors = chunkManagerClass.constructors
            for (constructor in constructors) {
                val types = arrayOf<Any>(World::class.java)
                if (Arrays.equals(constructor.parameterTypes, types)) {
                    worldChunkMgr = constructor.newInstance(worldObj) as WorldChunkManager
                } else if (constructor.parameterTypes.isEmpty()) {
                    worldChunkMgr = constructor.newInstance() as WorldChunkManager
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun setDimension(dim: Int) {
        dimensionId = dim
        super.setDimension(dim)
    }

    override fun getDimensionName(): String? {
        return getCelestialBody().getLocalizedName()
    }

    override fun isGasPresent(gas: AtmosphericGas): Boolean {
        return getCelestialBody().getAtmosphere().contains(gas)
    }

    open fun hasAtmosphere(): Boolean {
        return getCelestialBody().getAtmosphere().isNotEmpty()
    }

    override fun getSaveFolder(): String {
        return "SPACE_" + getCelestialBody().getDimensionID()
    }

    override fun canBlockFreeze(x: Int, y: Int, z: Int, byWater: Boolean): Boolean {
        return canRainOrSnow()
    }

    override fun canDoLightning(chunk: Chunk?): Boolean {
        return canRainOrSnow()
    }

    override fun canDoRainSnowIce(chunk: Chunk?): Boolean {
        return canRainOrSnow()
    }

    override fun calcSunriseSunsetColors(var1: Float, var2: Float): FloatArray? {
        return if (hasSunset()) super.calcSunriseSunsetColors(var1, var2) else null
    }

    override fun calculateCelestialAngle(worldTime: Long, partialTickTime: Float): Float {
        val offsetTime = worldObj.worldInfo.worldTime + timeCurrentOffset
        val j = (offsetTime % getDayLength()).toInt()
        var angleF1 = (j + partialTickTime) / getDayLength() - 0.25f
        if (angleF1 < 0.0f) {
            ++angleF1
        }
        if (angleF1 > 1.0f) {
            --angleF1
        }
        val angleF2 = angleF1
        angleF1 = 0.5f - MathHelper.cos(angleF1 * Math.PI.toFloat()) / 2.0f
        return angleF2 + (angleF1 - angleF2) / 3.0f
    }

    @SideOnly(Side.CLIENT)
    override fun getFogColor(var1: Float, var2: Float): Vec3? {
        val fogColor = this.getFogColor() ?: return null
        return Vec3.createVectorHelper(fogColor.x, fogColor.y, fogColor.z)
    }

    override fun getSkyColor(cameraEntity: Entity?, partialTicks: Float): Vec3? {
        val skyColor = this.getSkyColor() ?: return null
        return Vec3.createVectorHelper(skyColor.x, skyColor.y, skyColor.z)
    }

    override fun isSkyColored(): Boolean {
        return true
    }

    /**
     * Do not override this.
     *
     * Returns true on clients (to allow rendering of sky etc., maybe even clouds). Returns false on servers
     */
    override fun isSurfaceWorld(): Boolean {
        return worldObj != null && worldObj.isRemote
    }

    override fun canRespawnHere(): Boolean {
        return false
    }

    override fun getRespawnDimension(player: EntityPlayerMP?): Int {
        return if (this.shouldForceRespawn()) dimensionId else 0
    }

    open fun shouldForceRespawn(): Boolean {
        return !Config.isEnabledForceRespawn
    }

    override fun hasBreathableAtmosphere(): Boolean {
        return isGasPresent(Atmospheric.OXYGEN) && !isGasPresent(Atmospheric.CO2)
    }

    override fun shouldMapSpin(entity: String?, x: Double, y: Double, z: Double): Boolean {
        return false
    }

    override fun getSolarSize(): Float {
        return 1.0f / getCelestialBody().getRelativeDistanceFromCenter().unscale
    }

    override fun setWorldTime(time: Long) {
        worldObj.worldInfo.worldTime = time
        var diff = -timeCurrentOffset
        timeCurrentOffset = time - worldObj.worldInfo.worldTime
        diff += timeCurrentOffset
        if (diff != 0L) {
            saveTime()
            preTickTime = time
        }
        saveTCO = 0L
    }

    override fun getWorldTime(): Long {
        return worldObj.worldInfo.worldTime + timeCurrentOffset
    }

    open fun adjustTimeOffset(diff: Long) {
        timeCurrentOffset -= diff
        preTickTime += diff
        if (diff != 0L) {
            saveTime()
        }
    }

    /**
     * Save this world's custom time (from timeCurrentOffset) into this world villages.dat
     */
    private fun saveTime() {
        try {
            val vc = worldObj.villageCollectionObj
            tickCounter?.isAccessible = true
            tickCounter?.setInt(vc, worldTime.toInt())
            vc.markDirty()
        } catch (_: Exception) {
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getCloudRenderer(): IRenderHandler? {
        return null
    }
}