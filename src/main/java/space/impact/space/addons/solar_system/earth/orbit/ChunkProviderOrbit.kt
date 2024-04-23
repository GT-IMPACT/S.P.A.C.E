package space.impact.space.addons.solar_system.earth.orbit

import net.minecraft.block.Block
import net.minecraft.block.BlockFalling
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.IProgressUpdate
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.ChunkProviderGenerate
import java.util.*

class ChunkProviderOrbit(private val worldObj: World, par2: Long, par4: Boolean) : ChunkProviderGenerate(worldObj, par2, par4) {
    private val rand: Random = Random(par2)

    override fun unloadQueuedChunks(): Boolean {
        return false
    }

    override fun getLoadedChunkCount(): Int {
        return 0
    }

    override fun saveChunks(var1: Boolean, var2: IProgressUpdate): Boolean {
        return true
    }

    override fun canSave(): Boolean {
        return true
    }

    override fun provideChunk(par1: Int, par2: Int): Chunk {
        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L)
        val ids = arrayOfNulls<Block>(32768)
        Arrays.fill(ids, Blocks.air)
        val meta = ByteArray(32768)
        val var4 = Chunk(this.worldObj, ids, meta, par1, par2)
        val biomesArray = var4.biomeArray
        Arrays.fill(biomesArray, BiomeGenBaseOrbit.space.biomeID.toByte())
        var4.generateSkylightMap()
        return var4
    }

    override fun chunkExists(par1: Int, par2: Int): Boolean {
        return true
    }

    override fun populate(par1IChunkProvider: IChunkProvider, par2: Int, par3: Int) {
        BlockFalling.fallInstantly = true
        val k = par2 * 16
        val l = par3 * 16
        this.rand.setSeed(this.worldObj.seed)
        val i1 = this.rand.nextLong() / 2L * 2L + 1L
        val j1 = this.rand.nextLong() / 2L * 2L + 1L
        this.rand.setSeed(par2 * i1 + par3 * j1 xor this.worldObj.seed)
        if (k == 0 && l == 0) {
            WorldGenSpaceStation().generate(this.worldObj, this.rand, k, 62, l)
        }
        BlockFalling.fallInstantly = false
    }

    override fun makeString(): String {
        return "OrbitLevelSource"
    }

    override fun getPossibleCreatures(par1EnumCreatureType: EnumCreatureType, i: Int, j: Int, k: Int): MutableList<BiomeGenBase.SpawnListEntry>? {
        return null
    }
}