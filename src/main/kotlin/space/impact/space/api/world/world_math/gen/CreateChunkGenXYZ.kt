package space.impact.space.api.world.world_math.gen

import net.minecraft.block.Block
import space.impact.space.api.world.BiomeBase

abstract class CreateChunkGenXYZ {
    abstract fun gen(data: WorldGeneratorData)

    open fun setBlock(data: WorldGeneratorData, block: Block, meta: Byte) {
        data.chunkProvider.genSetBlock(data.chunkBlocks, data.chunkMetas, data.crX, data.crY, data.crZ, block, meta)
    }

    open fun getBlock(data: WorldGeneratorData): Block? {
        return data.chunkProvider.genReturnBlock(data.chunkBlocks, data.crX, data.crY, data.crZ)
    }

    open fun getBlockMeta(data: WorldGeneratorData): Byte {
        return data.chunkProvider.genReturnBlockMeta(data.chunkMetas, data.crX, data.crY, data.crZ)
    }

    open fun getBiome(data: WorldGeneratorData): BiomeBase? {
        return data.biomes[data.crX][data.crZ]
    }
}