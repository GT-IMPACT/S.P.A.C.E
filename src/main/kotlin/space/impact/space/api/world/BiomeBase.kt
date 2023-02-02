package space.impact.space.api.world

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.world.biome.BiomeGenBase
import space.impact.space.api.world.world_math.gen.CreateChunkGenXYZ
import space.impact.space.api.world.world_math.gen.CreateChunkGenXZ
import space.impact.space.api.world.world_math.perlin.PerlinNoiseGenerator

class BiomeBase(id: Int, isRegister: Boolean = false) : BiomeGenBase(id, isRegister) {

    companion object {
        private var chunkProvider: ChunkProviderExtension? = null

        fun setBiomeMap(cp: ChunkProviderExtension, persistence: Double, numOctaves: Int, sx: Double, sy: Double) {
            cp.biomemapPersistence = persistence
            cp.biomemapNumberOfOctaves = numOctaves
            cp.biomemapScaleX = sx
            cp.biomemapScaleY = sy
        }

        fun addBiomeToGeneration(cp: ChunkProviderExtension, biome: BiomeBase): Int {
            if (biome.biomeMaxValueOnMap < biome.biomeMinValueOnMap) {
                val t: Double = biome.biomeMaxValueOnMap
                biome.biomeMaxValueOnMap = biome.biomeMinValueOnMap
                biome.biomeMinValueOnMap = t
            }
            cp.biomesList.add(biome)
            biome.id = cp.biomesList.size
            if (cp.standardBiomeOnMap == null) {
                cp.standardBiomeOnMap = biome
            }
            return biome.id
        }

        fun setChunkProvider(prov: ChunkProviderExtension) {
            chunkProvider = prov
        }

        fun getBiggestInterpolationQuality(cp: ChunkProviderExtension): Int {
            var r = 0
            for (i in 0 until cp.biomesList.size) {
                if (cp.biomesList[i].biomeInterpolateQuality > r) {
                    r = cp.biomesList[i].biomeInterpolateQuality
                }
            }
            return r
        }

        fun getBiomeAt(cp: ChunkProviderExtension? = chunkProvider, x: Long, z: Long): BiomeBase? {
            if (cp == null) return null
            val biomeMapData: Double = PerlinNoiseGenerator.perlinNoise2D(
                cp.world.seed * 11L xor 0x6L,
                x / cp.biomemapScaleX,
                z / cp.biomemapScaleX,
                cp.biomemapPersistence,
                cp.biomemapNumberOfOctaves
            ) * cp.biomemapScaleY
            var r: BiomeBase? = null
            for (i in 0 until cp.biomesList.size) {
                if (biomeMapData >= cp.biomesList[i].biomeMinValueOnMap && biomeMapData <= cp.biomesList[i].biomeMaxValueOnMap) if (r != null) {
                    if (cp.biomesList[i].biomeMaxValueOnMap - cp.biomesList[i].biomeMinValueOnMap < r.biomeMaxValueOnMap - r.biomeMinValueOnMap) r = cp.biomesList[i]
                } else {
                    r = cp.biomesList[i]
                }
            }
            if (r == null) r = cp.standardBiomeOnMap
            return r
        }
    }

    var id = 0
    var biomeMinValueOnMap = -1.0
    var biomeMaxValueOnMap = 1.0
    var biomePersistence = 1.0

    var biomeScaleX = 1.0
    var biomeScaleY = 1.0

    var biomeNumberOfOctaves = 1
    var biomeSurfaceHeight = 63
    var biomeInterpolateQuality = 16

    var biomeGrassColor = 16777215

    var createChunkGenXZList: List<CreateChunkGenXZ> = ArrayList()
    var createChunkGenXYZList: List<CreateChunkGenXYZ> = ArrayList()
    var decorateChunkGenList: List<IWorldGenerator> = ArrayList()

    init {
        setBiomeName("S.P.A.C.E")

        spawnableCaveCreatureList.clear()
        spawnableCreatureList.clear()
        spawnableMonsterList.clear()
        spawnableWaterCreatureList.clear()

    }

}