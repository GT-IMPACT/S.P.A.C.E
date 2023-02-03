package space.impact.space.api.world.gen.chunk

import net.minecraft.world.ChunkPosition
import net.minecraft.world.biome.BiomeCache
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.biome.WorldChunkManager
import net.minecraft.world.gen.layer.IntCache
import java.util.*

abstract class SpaceWorldChunkManagerBase : WorldChunkManager() {

    protected val biomeCache: BiomeCache = BiomeCache(this)
    protected val biomesToSpawnIn: ArrayList<BiomeGenBase> = ArrayList()

    init {
        biomesToSpawnIn += getBiome()
    }

    override fun getBiomesToSpawnIn(): List<BiomeGenBase?>? {
        return this.biomesToSpawnIn
    }

    override fun getBiomeGenAt(par1: Int, par2: Int): BiomeGenBase? {
        return this.getBiome()
    }

    override fun getRainfall(par1ArrayOfFloat: FloatArray?, par2: Int, par3: Int, par4: Int, par5: Int): FloatArray? {
        var par1ArrayOfFloat = par1ArrayOfFloat
        if (par1ArrayOfFloat == null || par1ArrayOfFloat.size < par4 * par5) {
            par1ArrayOfFloat = FloatArray(par4 * par5)
        }
        Arrays.fill(par1ArrayOfFloat, 0, par4 * par5, 0.0f)
        return par1ArrayOfFloat
    }

    override fun getTemperatureAtHeight(par1: Float, par2: Int): Float {
        return par1
    }

    override fun getBiomesForGeneration(
        par1ArrayOfBiomeGenBase: Array<BiomeGenBase?>?, par2: Int, par3: Int, par4: Int,
        par5: Int
    ): Array<BiomeGenBase?>? {
        var par1ArrayOfBiomeGenBase = par1ArrayOfBiomeGenBase
        IntCache.resetIntCache()
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.size < par4 * par5) {
            par1ArrayOfBiomeGenBase = arrayOfNulls(par4 * par5)
        }
        for (var7 in 0 until par4 * par5) {
            par1ArrayOfBiomeGenBase[var7] = this.getBiome()
        }
        return par1ArrayOfBiomeGenBase
    }

    override fun loadBlockGeneratorData(par1ArrayOfBiomeGenBase: Array<BiomeGenBase?>?, par2: Int, par3: Int, par4: Int, par5: Int): Array<BiomeGenBase?> {
        return this.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true)
    }

    override fun getBiomeGenAt(par1ArrayOfBiomeGenBase: Array<BiomeGenBase?>?, par2: Int, par3: Int, par4: Int, par5: Int, par6: Boolean): Array<BiomeGenBase?> {
        var par1ArrayOfBiomeGenBase = par1ArrayOfBiomeGenBase
        IntCache.resetIntCache()
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.size < par4 * par5) {
            par1ArrayOfBiomeGenBase = arrayOfNulls(par4 * par5)
        }
        return if (par6 && par4 == 16 && par5 == 16 && par2 and 15 == 0 && par3 and 15 == 0) {
            val var9 = this.biomeCache.getCachedBiomes(par2, par3)
            System.arraycopy(var9, 0, par1ArrayOfBiomeGenBase, 0, par4 * par5)
            par1ArrayOfBiomeGenBase
        } else {
            for (var8 in 0 until par4 * par5) {
                par1ArrayOfBiomeGenBase[var8] = this.getBiome()
            }
            par1ArrayOfBiomeGenBase
        }
    }

    override fun areBiomesViable(par1: Int, par2: Int, par3: Int, par4List: List<*>): Boolean {
        return par4List.contains(this.getBiome())
    }

    override fun findBiomePosition(par1: Int, par2: Int, par3: Int, par4List: List<*>?, par5Random: Random?): ChunkPosition? {
        val var6 = par1 - par3 shr 2
        val var7 = par2 - par3 shr 2
        val var8 = par1 + par3 shr 2
        val var10 = var8 - var6 + 1
        val var16 = var6 + 0 % var10 shl 2
        val var17 = var7 + 0 / var10 shl 2
        return ChunkPosition(var16, 0, var17)
    }

    override fun cleanupCache() {
        this.biomeCache.cleanupCache()
    }


    abstract fun getBiome(): BiomeGenBase
}