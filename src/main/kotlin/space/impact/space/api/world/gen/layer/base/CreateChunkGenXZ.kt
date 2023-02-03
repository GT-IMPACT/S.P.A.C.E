package space.impact.space.api.world.gen.layer.base

import net.minecraft.block.Block
import space.impact.space.api.world.gen.biome.Biome

abstract class CreateChunkGenXZ {
    abstract fun gen(data: WorldGeneratorData)

    open fun setBlock(data: WorldGeneratorData, block: Block, meta: Byte, yInChunk: Int) {
        data.chunkProvider.genSetBlock(data.chunkBlocks, data.chunkMetas, data.crX, yInChunk, data.crZ, block, meta)
    }

    open fun getBlock(data: WorldGeneratorData, yInChunk: Int): Block? {
        return data.chunkProvider.genReturnBlock(data.chunkBlocks, data.crX, yInChunk, data.crZ)
    }

    open fun getBlockMeta(data: WorldGeneratorData, yInChunk: Int): Byte {
        return data.chunkProvider.genReturnBlockMeta(data.chunkMetas, data.crX, yInChunk, data.crZ)
    }

    open fun getBiome(data: WorldGeneratorData): Biome? {
        return data.biomes[data.crX][data.crZ]
    }
}