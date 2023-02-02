package space.impact.space.api.world.world_math.gen

import net.minecraft.block.Block
import space.impact.space.api.world.BiomeBase
import space.impact.space.api.world.ChunkProviderExtension
import space.impact.space.utils.Array2

class WorldGeneratorData(
    val chunkProvider: ChunkProviderExtension,
    val chunkBlocks: Array<Block?>,
    val chunkMetas: ByteArray,
    val biomes: Array2<BiomeBase?>,
    val chX: Long,
    val chZ: Long,
    val crX: Int,
    val crY: Int,
    val crZ: Int,
)
