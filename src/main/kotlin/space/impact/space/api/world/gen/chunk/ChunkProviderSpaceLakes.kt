package space.impact.space.api.world.gen.chunk

import net.minecraft.block.Block
import net.minecraft.block.BlockFalling
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.IProgressUpdate
import net.minecraft.util.MathHelper
import net.minecraft.world.ChunkPosition
import net.minecraft.world.SpawnerAnimals
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeCache
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.NoiseGeneratorOctaves
import net.minecraft.world.gen.NoiseGeneratorPerlin
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.other.BlockMetaPair
import space.impact.space.api.world.gen.other.EnumCraterSize
import space.impact.space.api.world.gen.other.GenBlocks
import space.impact.space.api.world.gen.world.MapGenMetaBase
import space.impact.space.api.world.world_math.perlin.Gradient
import java.util.*
import kotlin.math.abs

abstract class ChunkProviderSpaceLakes(
    protected val worldObj: World,
    seed: Long,
    flag: Boolean
) : IChunkProvider {

    companion object {
        const val CHUNK_SIZE_X = 16
        const val CHUNK_SIZE_Y = 256
        const val CHUNK_SIZE_Z = 16

        const val MAIN_FEATURE_FILTER_MOD = 4.0
        const val LARGE_FEATURE_FILTER_MOD = 8.0
        const val SMALL_FEATURE_FILTER_MOD = 8.0

        var genBlocks: ArrayList<GenBlocks> = ArrayList()
    }

    private val TERRAIN_HEIGHT_MOD: Double = this.getHeightModifier()
    private val SMALL_FEATURE_HEIGHT_MOD: Double = this.getSmallFeatureHeightModifier()
    private val MOUNTAIN_HEIGHT_MOD: Double = this.getMountainHeightModifier()
    private val VALLEY_HEIGHT_MOD: Double = this.getValleyHeightModifier()
    private val CRATER_PROB: Int = this.getCraterProbability()
    private val MID_HEIGHT: Int = this.getTerrainLevel()

    protected var rand: Random = Random(seed)
    private val myBiomeCache: BiomeCache? = null

    private val stoneNoise = DoubleArray(256)

    @JvmField
    protected var biomesForGeneration = getBiomesForGeneration()

    private var noiseGenOctaves4: NoiseGeneratorOctaves = NoiseGeneratorOctaves(this.rand, 4)
    private var noiseGenOctaves5: NoiseGeneratorOctaves = NoiseGeneratorOctaves(this.rand, 10)
    private var noiseGenOctaves6: NoiseGeneratorOctaves = NoiseGeneratorOctaves(this.rand, 16)

    private val noiseGenOctavesX: NoiseGeneratorOctaves = NoiseGeneratorOctaves(this.rand, 16)
    private val noiseGenOctavesY: NoiseGeneratorOctaves = NoiseGeneratorOctaves(this.rand, 16)
    private val noiseGenOctavesZ: NoiseGeneratorOctaves = NoiseGeneratorOctaves(this.rand, 8)
    private val noiseGenPerlinM: NoiseGeneratorPerlin = NoiseGeneratorPerlin(this.rand, 4)

    private val terrainCalc: DoubleArray = DoubleArray(825)
    private val parabolicField: FloatArray = FloatArray(25)

    private var octavesPickD: DoubleArray? = null
    private var octavesPickE: DoubleArray? = null
    private var octavesPickF: DoubleArray? = null
    private var octavesPickG: DoubleArray? = null

    private val noiseGen8: Gradient = Gradient(rand.nextLong(), 2, 0.25f)

    private val noiseGen1: Gradient = Gradient(this.rand.nextLong(), 4, 0.25F)
    private val noiseGen2: Gradient = Gradient(this.rand.nextLong(), 4, 0.25F)
    private val noiseGen3: Gradient = Gradient(this.rand.nextLong(), 4, 0.25F)
    private val noiseGen4: Gradient = Gradient(this.rand.nextLong(), 2, 0.25F)
    private val noiseGen5: Gradient = Gradient(this.rand.nextLong(), 1, 0.25F)
    private val noiseGen6: Gradient = Gradient(this.rand.nextLong(), 1, 0.25F)
    private val noiseGen7: Gradient = Gradient(this.rand.nextLong(), 1, 0.25F)

    @JvmField
    protected var worldGenerators: List<MapGenMetaBase>? = null

    init {
        for (j in -2..2) {
            for (k in -2..2) {
                val f = 10.0f / MathHelper.sqrt_float((j * j + k * k).toFloat() + 0.2f)
                parabolicField[j + 2 + (k + 2) * 5] = f
            }
        }
    }

    override fun provideChunk(x: Int, z: Int): Chunk? {
        rand.setSeed(x.toLong() * 341873128712L + z.toLong() * 132897987541L)
        val blockStorage = arrayOfNulls<Block>(65536)
        val metaStorage = ByteArray(65536)
        generateTerrain(x, z, blockStorage, metaStorage)
        if (getCraterProbability() > 0) {
            createCraters(x, z, blockStorage, metaStorage)
        }
        biomesForGeneration = worldObj.worldChunkManager.loadBlockGeneratorData(biomesForGeneration, x * 16, z * 16, 16, 16)

        replaceBlocksForBiome(x, z, blockStorage, metaStorage, biomesForGeneration)
        if (worldGenerators == null) {
            worldGenerators = getWorldGenerators()
        }
        val var5: Iterator<MapGenMetaBase> = worldGenerators!!.iterator()
        while (var5.hasNext()) {
            val generator = var5.next()
            generator.generate(this, worldObj, x, z, blockStorage, metaStorage)
        }
        onChunkProvider(x, z, blockStorage, metaStorage)
        val chunk = Chunk(worldObj, blockStorage, metaStorage, x, z)
        val chunkBiomes = chunk.biomeArray
        for (i in chunkBiomes.indices) {
            chunkBiomes[i] = biomesForGeneration[i].biomeID.toByte()
        }
        chunk.generateSkylightMap()
        return chunk
    }

    open fun generateTerrain(chunkX: Int, chunkZ: Int, blockStorage: Array<Block?>, metaStorage: ByteArray?) {
        biomesForGeneration = worldObj.worldChunkManager.loadBlockGeneratorData(biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10)
        makeLand(chunkX, chunkZ, blockStorage, ByteArray(65536))
    }

    open fun makeLand(chunkX: Int, chunkZ: Int, idArray: Array<Block?>, metaArray: ByteArray) {
        noiseGen1.setFrequency(0.015f)
        noiseGen2.setFrequency(0.01f)
        noiseGen3.setFrequency(0.01f)
        noiseGen4.setFrequency(0.01f)
        noiseGen5.setFrequency(0.01f)
        noiseGen6.setFrequency(0.001f)
        noiseGen7.setFrequency(0.005f)
        for (x in 0..15) {
            for (z in 0..15) {

                val baseHeight = noiseGen1.getNoise((chunkX * 16 + x).toFloat(), (chunkZ * 16 + z).toFloat()).toDouble() * TERRAIN_HEIGHT_MOD
                val smallHillHeight = noiseGen2.getNoise((chunkX * 16 + x).toFloat(), (chunkZ * 16 + z).toFloat()).toDouble() * SMALL_FEATURE_HEIGHT_MOD
                var mountainHeight = abs(noiseGen3.getNoise((chunkX * 16 + x).toFloat(), (chunkZ * 16 + z).toFloat())).toDouble()
                var valleyHeight = abs(noiseGen4.getNoise((chunkX * 16 + x).toFloat(), (chunkZ * 16 + z).toFloat())).toDouble()
                val featureFilter = noiseGen5.getNoise((chunkX * 16 + x).toFloat(), (chunkZ * 16 + z).toFloat()).toDouble() * 4.0
                val largeFilter = noiseGen6.getNoise((chunkX * 16 + x).toFloat(), (chunkZ * 16 + z).toFloat()).toDouble() * 8.0
                val smallFilter = noiseGen7.getNoise((chunkX * 16 + x).toFloat(), (chunkZ * 16 + z).toFloat()).toDouble() * 8.0 - 0.5

                mountainHeight = lerp(smallHillHeight, mountainHeight * MOUNTAIN_HEIGHT_MOD, fade(clamp(mountainHeight * 2.0, 0.0, 1.0)))
                valleyHeight = lerp(smallHillHeight, valleyHeight * VALLEY_HEIGHT_MOD - VALLEY_HEIGHT_MOD + 9.0, fade(clamp((valleyHeight + 2.0) * 4.0, 0.0, 1.0)))
                var yDev = lerp(valleyHeight, mountainHeight, fade(largeFilter))
                yDev = lerp(smallHillHeight, yDev, smallFilter)
                yDev = lerp(baseHeight, yDev, featureFilter)
                val biomes = worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16)
                for (y in 0..255) {
                    if (y.toDouble() < MID_HEIGHT.toDouble() + yDev) {
                        if (enableBiomeGenBaseBlock() && genBlocks.isNotEmpty()) {
                            var index = -1
                            val var26: Iterator<GenBlocks> = genBlocks.iterator()
                            while (var26.hasNext()) {
                                val genBlock: GenBlocks = var26.next()
                                ++index
                                if (worldObj.provider == genBlock.provider && biomes == genBlock.biome) {
                                    break
                                }
                            }
                            idArray[getIndex(x, y, z)] = genBlocks[index].stone.block
                            metaArray[getIndex(x, y, z)] = genBlocks[index].stone.meta
                        } else {
                            idArray[getIndex(x, y, z)] = getStoneBlock().block
                            metaArray[getIndex(x, y, z)] = getStoneBlock().meta
                        }
                    }
                }
            }
        }
    }

    open fun makeLandPerBiome2(x: Int, zero: Int, z: Int) {
        octavesPickG = noiseGenOctaves6.generateNoiseOctaves(octavesPickG, x, z, 5, 5, 200.0, 200.0, 0.5)
        octavesPickD = noiseGenOctavesZ.generateNoiseOctaves(octavesPickD, x, zero, z, 5, 33, 5, 8.555150000000001, 4.277575000000001, 8.555150000000001)
        octavesPickE = noiseGenOctavesX.generateNoiseOctaves(octavesPickE, x, zero, z, 5, 33, 5, 684.412, 684.412, 684.412)
        octavesPickF = noiseGenOctavesY.generateNoiseOctaves(octavesPickF, x, zero, z, 5, 33, 5, 684.412, 684.412, 684.412)
        var terrainIndex = 0
        var noiseIndex = 0

        for (ax in 0..4) {
            for (az in 0..4) {
                var totalVariation = 0.0f
                var totalHeight = 0.0f
                var totalFactor = 0.0f
                val two: Byte = 2
                val biomegenbase = biomesForGeneration[ax + 2 + (az + 2) * 10]
                for (ox in -two..two) {
                    for (oz in -two..two) {
                        val biomegenbase1 = biomesForGeneration[ax + ox + 2 + (az + oz + 2) * 10]
                        val rootHeight = biomegenbase1.rootHeight
                        val heightVariation = biomegenbase1.heightVariation
                        var heightFactor = parabolicField[ox + 2 + (oz + 2) * 5] / (rootHeight + 2.0f)
                        if (biomegenbase1.rootHeight > biomegenbase.rootHeight) {
                            heightFactor /= 2.0f
                        }
                        totalVariation += heightVariation * heightFactor
                        totalHeight += rootHeight * heightFactor
                        totalFactor += heightFactor
                    }
                }
                totalVariation /= totalFactor
                totalHeight /= totalFactor
                totalVariation = totalVariation * 0.9f + 0.1f
                totalHeight = (totalHeight * 4.0f - 1.0f) / 8.0f
                var terrainNoise = (octavesPickG?.get(noiseIndex) ?: 0.0) / 8000.0
                if (terrainNoise < 0.0) {
                    terrainNoise = -terrainNoise * 0.3
                }
                terrainNoise = terrainNoise * 3.0 - 2.0
                if (terrainNoise < 0.0) {
                    terrainNoise /= 2.0
                    if (terrainNoise < -1.0) {
                        terrainNoise = -1.0
                    }
                    terrainNoise /= 1.4
                    terrainNoise /= 2.0
                } else {
                    if (terrainNoise > 1.0) {
                        terrainNoise = 1.0
                    }
                    terrainNoise /= 8.0
                }
                ++noiseIndex
                var heightCalc = totalHeight.toDouble()
                val variationCalc = totalVariation.toDouble() * getHeightModifier() / 10.0
                heightCalc += terrainNoise * 0.2
                heightCalc = heightCalc * 8.5 / 8.0
                val d5 = 8.5 + heightCalc * 4.0
                for (ay in 0..32) {
                    var d6 = (ay.toDouble() - d5) * 12.0 * 128.0 / 256.0 / variationCalc
                    if (d6 < 0.0) {
                        d6 *= 4.0
                    }
                    val d7 = (octavesPickE?.get(terrainIndex) ?: 0.0) / 512.0
                    val d8 = (octavesPickF?.get(terrainIndex) ?: 0.0) / 512.0
                    val d9 = ((octavesPickD?.get(terrainIndex) ?: 0.0) / 10.0 + 1.0) / 2.0
                    var terrainCalc = MathHelper.denormalizeClamp(d7, d8, d9) - d6
                    if (ay > 29) {
                        val d11 = ((ay - 29).toFloat() / 3.0f).toDouble()
                        terrainCalc = terrainCalc * (1.0 - d11) + -10.0 * d11
                    }
                    this.terrainCalc[terrainIndex] = terrainCalc
                    ++terrainIndex
                }
            }
        }
    }

    open fun replaceBlocksForBiome(par1: Int, par2: Int, arrayOfIDs: Array<Block?>, arrayOfMeta: ByteArray, par4ArrayOfBiomeGenBase: Array<BiomeGenBase>) {
        noiseGen8.setFrequency(0.0625f)

        var var8 = 0
        var var9: Int
        var var12: Int

        var var14: Block
        var var14m: Byte

        while (var8 < 16) {
            var9 = 0
            while (var9 < 16) {
                var12 = (noiseGen8.getNoise((par1 * 16 + var8).toFloat(), (par2 * 16 + var9).toFloat()).toDouble() / 3.0 + getDirtLayerSize() + rand.nextDouble() * 0.25).toInt()
                var var13 = -1
                var var15: Block?
                var var15m: Byte
                var stone: Block
                var mStone: Byte
                var var16: Int
                if (enableBiomeGenBaseBlock() && genBlocks.isNotEmpty()) {
                    var16 = -1
                    val var20: Iterator<*> = genBlocks.iterator()
                    while (var20.hasNext()) {
                        val genBlock: GenBlocks = var20.next() as GenBlocks
                        ++var16
                        if (worldObj.provider == genBlock.provider && par4ArrayOfBiomeGenBase[var8 + var9 * 16] == genBlock.biome) {
                            break
                        }
                    }
                    var14 = genBlocks[var16].grass.block
                    var14m = genBlocks[var16].grass.meta
                    var15 = genBlocks[var16].dirt.block
                    var15m = genBlocks[var16].dirt.meta
                    stone = genBlocks[var16].stone.block
                    mStone = genBlocks[var16].stone.meta
                } else {
                    var14 = getGrassBlock().block
                    var14m = getGrassBlock().meta
                    var15 = getDirtBlock().block
                    var15m = getDirtBlock().meta
                    stone = getStoneBlock().block
                    mStone = getStoneBlock().meta
                }
                var16 = 255
                while (var16 >= 0) {
                    val index = getIndex(var8, var16, var9)
                    if (canGenerateIceBlock() && (var16 == 1 || var16 == 1 + rand.nextInt(8))) {
                        arrayOfIDs[index] = postBedrockBlock().block
                        arrayOfMeta[index] = postBedrockBlock().meta
                    }
                    if (var16 <= 0) {
                        arrayOfIDs[index] = Blocks.bedrock
                    } else {
                        val var18 = arrayOfIDs[index]
                        if (Blocks.air == var18) {
                            var13 = -1
                        } else if (var18 == stone) {
                            arrayOfMeta[index] = mStone
                            if (var13 != -1) {
                                if (var13 > 0) {
                                    --var13
                                    arrayOfIDs[index] = var15
                                    arrayOfMeta[index] = var15m
                                }
                            } else {
                                if (var12 <= 0) {
                                    var14 = Blocks.air
                                    var14m = 0
                                    var15 = stone
                                    var15m = mStone
                                } else if (var16 in 21..36) {
                                    if (enableBiomeGenBaseBlock() && genBlocks.isNotEmpty()) {
//                                        var14 = genBlocks[index].grass.block
//                                        var14m = genBlocks[index].grass.meta
                                        var14 = genBlocks[index].dirt.block
                                        var14m = genBlocks[index].dirt.meta
                                    } else {
//                                        var14 = getGrassBlock().block
//                                        var14m = getGrassBlock().meta
                                        var14 = getDirtBlock().block
                                        var14m = getDirtBlock().meta
                                    }
                                }
                                var13 = var12
                                arrayOfIDs[index] = var14
                                arrayOfMeta[index] = var14m
                            }
                        } else if (var16 < getWaterLevel() && canGenerateWaterBlock()) {
                            arrayOfIDs[index] = getWaterBlock().block
                            arrayOfMeta[index] = getWaterBlock().meta
                        }
                    }
                    --var16
                }
                ++var9
            }
            ++var8
        }
    }

    open fun createCraters(chunkX: Int, chunkZ: Int, chunkArray: Array<Block?>, metaArray: ByteArray) {
        noiseGen5.setFrequency(0.015f)
        for (cx in chunkX - 2..chunkX + 2) {
            for (cz in chunkZ - 2..chunkZ + 2) {
                for (x in 0..15) {
                    for (z in 0..15) {
                        if (abs(randFromPoint(cx * 16 + x, (cz * 16 + z) * 1000)) < (noiseGen5.getNoise((cx * 16 + x).toFloat(), (cz * 16 + z).toFloat()) / CRATER_PROB.toFloat()).toDouble()) {
                            val random = Random((cx * 16 + x + (cz * 16 + z) * 5000).toLong())
                            val cSize = EnumCraterSize.sizeArray.random()
                            if (cSize != null) {
                                val size: Int = random.nextInt(cSize.max - cSize.min) + cSize.min + 15
                                makeCrater(cx * 16 + x, cz * 16 + z, chunkX * 16, chunkZ * 16, size, chunkArray, metaArray)
                            }
                        }
                    }
                }
            }
        }
    }

    open fun makeCrater(craterX: Int, craterZ: Int, chunkX: Int, chunkZ: Int, size: Int, chunkArray: Array<Block?>, metaArray: ByteArray) {
        for (x in 0..15) {
            for (z in 0..15) {
                var xDev = (craterX - (chunkX + x)).toDouble()
                var zDev = (craterZ - (chunkZ + z)).toDouble()
                if (xDev * xDev + zDev * zDev < (size * size).toDouble()) {
                    xDev /= size.toDouble()
                    zDev /= size.toDouble()
                    val sqrtY = xDev * xDev + zDev * zDev
                    var yDev = sqrtY * sqrtY * 6.0
                    yDev = 5.0 - yDev
                    var helper = 0
                    var y = 127
                    while (y > 0 && helper.toDouble() <= yDev) {
                        if (chunkArray[getIndex(x, y, z)] != null) {
                            getCraterAdditions(chunkArray, metaArray, x, y, z)
                            chunkArray[getIndex(x, y, z)] = Blocks.air
                            metaArray[getIndex(x, y, z)] = 0
                            ++helper
                        }
                        --y
                    }
                }
            }
        }
    }

    open fun randFromPoint(x: Int, z: Int): Double {
        var n = x + z * 57
        n = n xor (n shl 13)
        return 1.0 - (n * (n * n * 15731 + 789221) + 1376312589 and Int.MAX_VALUE).toDouble() / 1.073741824E9
    }

    protected open fun getIndex(x: Int, y: Int, z: Int): Int {
        return (x * 16 + z) * 256 + y
    }

    override fun loadChunk(x: Int, z: Int): Chunk? {
        return provideChunk(x, z)
    }

    override fun chunkExists(x: Int, z: Int): Boolean {
        return true
    }

    override fun populate(chunk: IChunkProvider?, x: Int, z: Int) {
        BlockFalling.fallInstantly = true
        val var4 = x * 16
        val var5 = z * 16
        val biomeGen: BiomeGenBase = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16)
        this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16)
        this.rand.setSeed(this.worldObj.seed)
        val var7: Long = this.rand.nextLong() / 2L * 2L + 1L
        val var9: Long = this.rand.nextLong() / 2L * 2L + 1L
        this.rand.setSeed(x.toLong() * var7 + z.toLong() * var9 xor this.worldObj.seed)
        biomeGen.decorate(this.worldObj, this.rand, var4, var5)
        decoratePlanet(this.worldObj, this.rand, var4, var5)
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomeGen, var4 + 8, var5 + 8, 16, 16, this.rand)
        onPopulate(chunk, x, z)
        BlockFalling.fallInstantly = false
    }

    open fun decoratePlanet(world: World?, rand: Random?, x: Int, z: Int) {
        getBiomeGenerator()?.decorate(world, rand, x, z)
    }

    override fun saveChunks(flag: Boolean, progress: IProgressUpdate?): Boolean {
        return true
    }

    override fun canSave(): Boolean {
        return true
    }

    override fun makeString(): String? {
        return "RandomLevelSource"
    }

    override fun getPossibleCreatures(type: EnumCreatureType?, x: Int, y: Int, z: Int): MutableList<Any?>? {
        return null
    }

    override fun getLoadedChunkCount(): Int {
        return 0
    }

    override fun recreateStructures(x: Int, z: Int) {}

    override fun unloadQueuedChunks(): Boolean {
        return false
    }

    override fun saveExtraData() {}

    override fun func_147416_a(world: World?, string: String?, x: Int, y: Int, z: Int): ChunkPosition? {
        return null
    }

    open fun setBlocks(array: GenBlocks) {
        genBlocks.add(array)
    }

    protected open fun lerp(d1: Double, d2: Double, t: Double): Double {
        return if (t < 0.0) {
            d1
        } else {
            if (t > 1.0) d2 else d1 + (d2 - d1) * t
        }
    }

    protected open fun fade(n: Double): Double {
        return n * n * n * (n * (n * 6.0 - 15.0) + 10.0)
    }

    protected open fun clamp(x: Double, min: Double, max: Double): Double {
        return if (x < min) min else {
            if (x > max) max else x
        }
    }



    protected abstract fun getBiomeGenerator(): BiomeDecoratorSpaceBase?

    protected abstract fun getBiomesForGeneration(): Array<BiomeGenBase>

    abstract fun onChunkProvider(var1: Int, var2: Int, var3: Array<Block?>?, var4: ByteArray?)

    abstract fun onPopulate(var1: IChunkProvider?, var2: Int, var3: Int)

    protected abstract fun getMonsters(): Array<SpawnListEntry?>?

    protected abstract fun getCreatures(): Array<SpawnListEntry?>?

    protected abstract fun getWaterCreatures(): Array<SpawnListEntry?>?

    protected abstract fun getWorldGenerators(): List<MapGenMetaBase>?

    abstract fun getHeightModifier(): Double

    abstract fun getSmallFeatureHeightModifier(): Double

    abstract fun getMountainHeightModifier(): Double

    abstract fun getValleyHeightModifier(): Double

    abstract fun getWaterLevel(): Int

    abstract fun canGenerateWaterBlock(): Boolean

    abstract fun canGenerateIceBlock(): Boolean

    abstract fun getCraterProbability(): Int

    protected abstract fun getWaterBlock(): BlockMetaPair

    protected abstract fun getGrassBlock(): BlockMetaPair

    protected abstract fun getDirtBlock(): BlockMetaPair

    protected abstract fun getStoneBlock(): BlockMetaPair

    protected abstract fun enableBiomeGenBaseBlock(): Boolean

    open fun getTerrainLevel(): Int {
        return getWaterLevel()
    }

    open fun getDirtLayerSize(): Double {
        return 3.0
    }

    protected open fun postBedrockBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.packed_ice, 0)
    }

    open fun getCraterAdditions(chunkArray: Array<Block?>?, metaArray: ByteArray?, x: Int, y: Int, z: Int) {}
}