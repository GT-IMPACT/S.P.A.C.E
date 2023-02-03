package space.impact.space.api.world.gen.layer.base

import net.minecraft.block.Block
import space.impact.space.api.world.gen.biome.Biome
import space.impact.space.api.world.gen.chunk.ChunkProviderBase
import space.impact.space.utils.Array2

class WorldGeneratorData(
    val chunkProvider: ChunkProviderBase,
    val chunkBlocks: Array<Block?>,
    val chunkMetas: ByteArray,
    val biomes: Array2<Biome?>,
    val chX: Long,
    val chZ: Long,
    val crX: Int,
    val crY: Int,
    val crZ: Int,
)
