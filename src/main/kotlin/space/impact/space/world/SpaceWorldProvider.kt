package space.impact.space.world

import net.minecraft.init.Blocks
import net.minecraft.village.VillageCollection
import net.minecraft.world.WorldProvider
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.biome.WorldChunkManagerHell
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.ChunkProviderFlat
import java.lang.reflect.Field

open class SpaceWorldProvider : WorldProvider() {

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
     * Save this world's custom time (from timeCurrentOffset) into this world villages.dat
     */
    private fun saveTime() {
        try {
            val vc = worldObj.villageCollectionObj
            tickCounter?.isAccessible = true
            tickCounter?.setInt(vc, worldTime.toInt())
            vc.markDirty()
        } catch (_: Exception) {}
    }

    /**
     * Weather or not there will be rain or snow in this dim
     */
    open fun canRainOrSnow(): Boolean {
        return true
    }

    override fun registerWorldChunkManager() {
        worldChunkMgr = WorldChunkManagerHell(BiomeGenBase.mesa, 0.0f)
        isHellWorld = false
        dimensionId = 2
    }

    override fun getSaveFolder(): String {
        return super.getSaveFolder()
    }

    override fun createChunkGenerator(): IChunkProvider {
        return ChunkProviderFlat(worldObj, worldObj.seed, worldObj.worldInfo.isMapFeaturesEnabled, worldObj.worldInfo.generatorOptions)
    }

    override fun isSurfaceWorld(): Boolean {
        return false
    }

    override fun canCoordinateBeSpawn(x: Int, z: Int): Boolean {
        return worldObj.getTopBlock(x, z) == Blocks.stone
    }

    override fun getDimensionName(): String {
        return "Space"
    }
}