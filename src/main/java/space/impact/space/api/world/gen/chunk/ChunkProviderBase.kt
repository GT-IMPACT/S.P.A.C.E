package space.impact.space.api.world.gen.chunk

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.block.Block
import net.minecraft.block.BlockFalling
import net.minecraft.entity.EnumCreatureType
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.ChunkProviderGenerate
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.terraingen.PopulateChunkEvent
import space.impact.space.api.world.gen.world.MapGenMetaBase
import space.impact.space.api.world.gen.biome.Biome
import space.impact.space.api.world.gen.biome.Biome.Companion.getBiomeAt
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.layer.base.CreateChunkGen
import space.impact.space.api.world.gen.layer.base.CreateChunkGenXYZ
import space.impact.space.api.world.gen.layer.base.CreateChunkGenXZ
import space.impact.space.api.world.gen.layer.base.WorldGeneratorData
import space.impact.space.utils.array2OfNulls
import java.util.*

open class ChunkProviderBase(
    override val world: World,
    private val seed: Long,
    private val isMapFeature: Boolean,
) : ChunkProviderGenerate(world, seed, isMapFeature), ChunkProviderExtension {

    companion object {
        private const val CHUNK_SIZE_X = 16
        private const val CHUNK_SIZE_Y = 255
        private const val CHUNK_SIZE_Z = 16
    }

    val rand: Random = Random(seed)

    protected var createChunkGenList: ArrayList<CreateChunkGen> = ArrayList()
    protected var createChunkGenXZList: ArrayList<CreateChunkGenXZ> = ArrayList()
    protected var createChunkGenXYZList: ArrayList<CreateChunkGenXYZ> = ArrayList()
    protected var decorateChunkGenList: ArrayList<IWorldGenerator> = ArrayList()

    override var biomemapPersistence = 1.0
    override var biomemapScaleX = 1.0
    override var biomemapScaleY = 1.0
    override var biomemapNumberOfOctaves = 1

    override var worldGenerators: ArrayList<MapGenMetaBase> = ArrayList()

    override var biomesList: ArrayList<Biome> = ArrayList()
    override var standardBiomeOnMap: Biome? = null

    private var currentBiome: BiomeGenBase? = null

    protected val decorator: BiomeDecoratorSpaceBase? = null

    override fun provideChunk(x: Int, z: Int): Chunk {
        val chX = x * 16L
        val chZ = z * 16L

        val chunkBlocks = arrayOfNulls<Block>(65536)
        val chunkBlocksMeta = ByteArray(65536)

        this.rand.setSeed(world.seed * x xor (3 + z).toLong() xor 0x758CF9L)

        val chunkBiomes = array2OfNulls<Biome>(16, 16)

        for (j in 0 until CHUNK_SIZE_X) {
            for (z in 0 until CHUNK_SIZE_Z) {
                chunkBiomes[j][z] = getBiomeAt(this, chX + j, chZ + z)
                currentBiome = chunkBiomes[j][z]
            }
        }

        for (chGen in createChunkGenList) {
            val data = WorldGeneratorData(this, chunkBlocks, chunkBlocksMeta, chunkBiomes, chX, chZ, 0, 0, 0)
            chGen.gen(data)
        }

        for (x in 0 until CHUNK_SIZE_X) {
            for (z in 0 until CHUNK_SIZE_Z) {

                for (createChunkGenXZ in createChunkGenXZList) {
                    val data = WorldGeneratorData(this, chunkBlocks, chunkBlocksMeta, chunkBiomes, chX, chZ, x, 0, z)
                    createChunkGenXZ.gen(data)
                }

                var biome = chunkBiomes[x][z]
                if (biome != null) {
                    for (createChunkGenXZ in biome.createChunkGenXZList) {
                        val data = WorldGeneratorData(this, chunkBlocks, chunkBlocksMeta, chunkBiomes, chX, chZ, x, 0, z)
                        createChunkGenXZ.gen(data)
                    }
                }

                for (y in CHUNK_SIZE_Y downTo 0) {
                    for (createChunkGenXYZ in createChunkGenXYZList) {
                        val data = WorldGeneratorData(this, chunkBlocks, chunkBlocksMeta, chunkBiomes, chX, chZ, x, y, z)
                        createChunkGenXYZ.gen(data)
                    }

                    biome = chunkBiomes[x][z]
                    if (biome != null) {
                        for (createChunkGenXYZ in biome.createChunkGenXYZList) {
                            val data = WorldGeneratorData(this, chunkBlocks, chunkBlocksMeta, chunkBiomes, chX, chZ, x, y, z)
                            createChunkGenXYZ.gen(data)
                        }
                    }
                }
            }
        }

        for (generator in worldGenerators) {
            generator.generate(this, world, chX.toInt(), chZ.toInt(), chunkBlocks, chunkBlocksMeta)
        }

        val var4 = Chunk(world, chunkBlocks, chunkBlocksMeta, x, z)
        var4.generateSkylightMap()
        return var4
    }


    override fun populate(chunkProvider: IChunkProvider?, x: Int, z: Int) {
        BlockFalling.fallInstantly = true

        this.rand.setSeed(world.seed * x + z xor 0x248B36L)
        MinecraftForge.EVENT_BUS.post(PopulateChunkEvent.Pre(chunkProvider, world, this.rand, x, z, false))

        val var7 = this.rand.nextLong() / 2L * 2L + 1L
        val var9 = this.rand.nextLong() / 2L * 2L + 1L
        this.rand.setSeed(x * var7 + z * var9 xor world.seed)

        for (gen in decorateChunkGenList) {
            gen.generate(rand, x, z, world, this, this)
        }

        val biome = getBiomeAt(this, x * 16L + rand.nextInt(16), z + 16L + rand.nextInt(16))
        if (biome != null) {
            for (gen in biome.decorateChunkGenList) {
                gen.generate(rand, x, z, world, this, this)
            }
        }

        decorateWorld(world, this.rand, x * 16, z * 16)

        MinecraftForge.EVENT_BUS.post(PopulateChunkEvent.Post(chunkProvider, world, this.rand, x, z, false))
        BlockFalling.fallInstantly = false
    }

    private fun decorateWorld(world: World?, rand: Random?, x: Int, z: Int) {
        getBiomeGenerator()?.decorate(world, rand, x, z)
    }

    protected open fun getBiomeGenerator(): BiomeDecoratorSpaceBase? {
        return this.decorator
    }

    override fun getPossibleCreatures(par1EnumCreatureType: EnumCreatureType?, x: Int, y: Int, z: Int): MutableList<BiomeGenBase.SpawnListEntry>? {
        /*val biome = getBiomeAt(this, x.toLong(), z.toLong())
        if (biome != null) {
            return biome.getSpawnableList(par1EnumCreatureType)
        }*/
        return null
    }
}