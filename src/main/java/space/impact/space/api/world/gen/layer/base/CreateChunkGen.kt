package space.impact.space.api.world.gen.layer.base

import net.minecraft.block.Block
import space.impact.space.api.world.gen.biome.Biome

abstract class CreateChunkGen {
    abstract fun gen(data: WorldGeneratorData)

    open fun setBlock(data: WorldGeneratorData, block: Block, meta: Byte, xInChunk: Int, yInChunk: Int, zInChunk: Int) {
        data.chunkProvider.genSetBlock(data.chunkBlocks, data.chunkMetas, xInChunk, yInChunk, zInChunk, block, meta)
    }

    open fun getBlock(data: WorldGeneratorData, xInChunk: Int, yInChunk: Int, zInChunk: Int): Block? {
        return data.chunkProvider.genReturnBlock(data.chunkBlocks, xInChunk, yInChunk, zInChunk)
    }

    open fun getBlockMeta(data: WorldGeneratorData, xInChunk: Int, yInChunk: Int, zInChunk: Int): Byte {
        return data.chunkProvider.genReturnBlockMeta(data.chunkMetas, xInChunk, yInChunk, zInChunk)
    }

    open fun getBiome(data: WorldGeneratorData, xInChunk: Int, zInChunk: Int): Biome? {
        return data.biomes[xInChunk][zInChunk]
    }
}