package space.impact.space.api.world.gen.biome

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.init.Blocks
import net.minecraft.world.biome.BiomeGenBase
import space.impact.space.api.world.gen.chunk.ChunkProviderExtension
import space.impact.space.api.world.gen.layer.base.CreateChunkGenXYZ
import space.impact.space.api.world.gen.layer.base.CreateChunkGenXZ
import space.impact.space.api.world.gen.layer.standart.*
import space.impact.space.api.world.world_math.perlin.PerlinNoiseGenerator

open class Biome(id: Int, isRegister: Boolean = false) : BiomeGenBase(id, isRegister) {

    companion object {
        private var chunkProvider: ChunkProviderExtension? = null

        fun setBiomeMap(cp: ChunkProviderExtension, persistence: Double, numOctaves: Int, sx: Double, sy: Double) {
            cp.biomemapPersistence = persistence
            cp.biomemapNumberOfOctaves = numOctaves
            cp.biomemapScaleX = sx
            cp.biomemapScaleY = sy
        }

        fun addBiomeToGeneration(cp: ChunkProviderExtension, biome: Biome): Int {
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

        fun getBiomeAt(cp: ChunkProviderExtension? = chunkProvider, x: Long, z: Long): Biome? {
            if (cp == null) return null
            val biomeMapData: Double = PerlinNoiseGenerator.perlinNoise2D(
                cp.world.seed * 11L xor 0x6L,
                x / cp.biomemapScaleX,
                z / cp.biomemapScaleX,
                cp.biomemapPersistence,
                cp.biomemapNumberOfOctaves
            ) * cp.biomemapScaleY
            var r: Biome? = null
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

    var createChunkGenXZList: ArrayList<CreateChunkGenXZ> = ArrayList()
    var createChunkGenXYZList: ArrayList<CreateChunkGenXYZ> = ArrayList()
    var decorateChunkGenList: ArrayList<IWorldGenerator> = ArrayList()

    init {
        setBiomeName("S.P.A.C.E")

        spawnableCaveCreatureList.clear()
        spawnableCreatureList.clear()
        spawnableMonsterList.clear()
        spawnableWaterCreatureList.clear()

        val standardBiomeLayers = BiomeLayer()
        standardBiomeLayers.add(Blocks.dirt, 0, Blocks.stone, 0, -256, 0, -4, -2, true)
        standardBiomeLayers.add(Blocks.grass, 0, Blocks.dirt, 0, -256, 0, -256, 0, false)
        standardBiomeLayers.add(Blocks.bedrock, 0,0, 2, 0, 0, true)

        createChunkGenXZList += standardBiomeLayers
        createChunkGenXZList += SnowGen()

        decorateChunkGenList += LakeGen()

        val treeGen = WorldTreeGen()
        treeGen.add(
            Blocks.log, 0, Blocks.leaves, 0, Blocks.sapling, Blocks.vine, Blocks.cocoa,
            8, 1, 8, 4, false,
            2, 0, 0, 1, 2, 1,
            1, 12, 4, 0.618, 0.381, 1.0, 1.0
        )
        decorateChunkGenList += treeGen

        val grassGen = GrassGen()
        grassGen.add(Blocks.tallgrass, 1, 8, false, Blocks.grass, 0)
        grassGen.add(Blocks.tallgrass, 2, 16, false, Blocks.grass, 0)
        grassGen.add(Blocks.red_flower, 0, 128, false, Blocks.grass, 0)
        grassGen.add(Blocks.yellow_flower, 0, 128, false, Blocks.grass, 0)
        grassGen.add(Blocks.deadbush, 0, 512, false, Blocks.grass, 0)
        grassGen.add(Blocks.waterlily, 0, 256, false, Blocks.water, 0)
        decorateChunkGenList += grassGen
    }
}