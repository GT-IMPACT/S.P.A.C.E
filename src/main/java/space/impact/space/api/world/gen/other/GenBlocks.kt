package space.impact.space.api.world.gen.other

import net.minecraft.world.WorldProvider
import net.minecraft.world.biome.BiomeGenBase

class GenBlocks(
    val provider: WorldProvider,
    val biome: BiomeGenBase,
    val grass: BlockMetaPair,
    val dirt: BlockMetaPair,
    val stone: BlockMetaPair,
)