package space.impact.space.api.world

import net.minecraft.block.Block
import net.minecraft.world.World

interface ChunkProviderExtension {

    val world: World
    var biomemapPersistence: Double
    var biomemapScaleX: Double
    var biomemapScaleY: Double
    var biomemapNumberOfOctaves: Int

    var worldGenerators: ArrayList<MapGenMetaBase>

    var biomesList: ArrayList<BiomeBase>
    var standardBiomeOnMap: BiomeBase?

    fun genSetBlock(chunkBlocks: Array<Block?>, chunkBlocksMeta: ByteArray, x: Int, y: Int, z: Int, block: Block, meta: Byte) {
        if (x in 0..15 && y >= 0 && y <= 255 && z >= 0 && z <= 15) {
            chunkBlocks[(x * 16 + z) * 256 + y] = block
            chunkBlocksMeta[(x * 16 + z) * 256 + y] = meta
        }
    }

    fun genReturnBlock(chunkBlocks: Array<Block?>, x: Int, y: Int, z: Int): Block? {
        return if (x in 0..15 && y >= 0 && y <= 255 && z >= 0 && z <= 15) {
            chunkBlocks[(x * 16 + z) * 256 + y]
        } else null
    }

    fun genReturnBlockMeta(chunkBlocksMeta: ByteArray, x: Int, y: Int, z: Int): Byte {
        return if (x in 0..15 && y >= 0 && y <= 255 && z >= 0 && z <= 15) {
            chunkBlocksMeta[(x * 16 + z) * 256 + y]
        } else 0
    }
}