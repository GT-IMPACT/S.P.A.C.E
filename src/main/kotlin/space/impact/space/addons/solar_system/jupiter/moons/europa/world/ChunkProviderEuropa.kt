package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry
import net.minecraft.world.chunk.IChunkProvider
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.biome.SpaceBiomes
import space.impact.space.api.world.gen.chunk.ChunkProviderSpaceLakes
import space.impact.space.api.world.gen.other.BlockMetaPair
import space.impact.space.api.world.gen.other.GenBlocks
import space.impact.space.api.world.gen.world.MapGenMetaBase

class ChunkProviderEuropa(world: World, seed: Long, flag: Boolean) : ChunkProviderSpaceLakes(world, seed, flag) {

    companion object {
        private val BLOCK_SURFACE = SolarSystem.EUROPA_BLOCKS
        private const val BLOCK_META_SURFACE: Byte = 0

        private val BLOCK_ICE = SolarSystem.EUROPA_BLOCKS
        private const val BLOCK_META_ICE: Byte = 1
    }

    private val ravineGenerator = MapGenRavineEuropa()

    init {
        setBlocks(GenBlocks(worldObj.provider, getBiomesForGeneration().first(), getGrassBlock(), getDirtBlock(), getStoneBlock()))
    }

    override fun getWorldGenerators(): List<MapGenMetaBase> {
        return listOf(ravineGenerator)
    }

    override fun getBiomeGenerator(): BiomeDecoratorSpaceBase {
        return BiomeDecoratorEuropaOre()
    }

    override fun getBiomesForGeneration(): Array<BiomeGenBase> {
        return arrayOf(SpaceBiomes.SPACE.setBiomeName("Europa"))
    }

    override fun getCraterProbability(): Int {
        return 80
    }

    override fun getCreatures(): Array<SpawnListEntry?> {
        return arrayOfNulls(0)
    }

    override fun getHeightModifier(): Double {
        return 15.0
    }

    override fun getMonsters(): Array<SpawnListEntry?> {
        return arrayOf()
    }

    override fun getMountainHeightModifier(): Double {
        return 35.0
    }

    override fun getSmallFeatureHeightModifier(): Double {
        return 50.0
    }

    override fun getValleyHeightModifier(): Double {
        return 5.0
    }

    override fun onChunkProvider(cX: Int, cZ: Int, blocks: Array<Block?>, metadata: ByteArray) {}

    override fun onPopulate(provider: IChunkProvider?, chX: Int, chZ: Int) {
        val k = chX * 16
        val l = chZ * 16
        for (k1 in 0..15) {
            for (l1 in 0..15) {
                val i2 = worldObj.getTopSolidOrLiquidBlock(k + k1, l + l1)
                if (i2 > getWaterLevel() + 2 || i2 >= getWaterLevel() - 5 && i2 < getWaterLevel() + 3) {
                    if (worldObj.getBlock(k1 + k, i2 - 1, l1 + l) == BLOCK_SURFACE
                        && worldObj.getBlockMetadata(k1 + k, i2 - 1, l1 + l) == 0
                    ) {
                        worldObj.setBlock(k1 + k, i2, l1 + l, Blocks.snow, 0, 2)
                        worldObj.setBlock(k1 + k, i2 + 1, l1 + l, Blocks.snow_layer, 0, 2)
                    }
                } else if (i2 < getWaterLevel() - 6 && worldObj.getBlock(k1 + k, i2 - 1, l1 + l) == BLOCK_SURFACE
                    && worldObj.getBlockMetadata(k1 + k, i2 - 1, l1 + l) == 0
                ) {
                    worldObj.setBlock(k1 + k, i2, l1 + l, Blocks.packed_ice, 0, 2)
                }
            }
        }
    }

    override fun getWaterCreatures(): Array<SpawnListEntry?> {
        return arrayOfNulls(0)
    }

    override fun getGrassBlock(): BlockMetaPair {
        return BlockMetaPair(BLOCK_SURFACE, BLOCK_META_SURFACE)
    }

    override fun getDirtBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.packed_ice, 0)
    }

    override fun getStoneBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.water, 0)
    }

    override fun enableBiomeGenBaseBlock(): Boolean {
        return true
    }

    override fun getWaterLevel(): Int {
        return 95
    }

    override fun canGenerateWaterBlock(): Boolean {
        return false
    }

    override fun canGeneratePostBedrock(): Boolean {
        return true
    }

    override fun getWaterBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.ice, 1)
    }

    override fun getDirtLayerSize(): Double {
        return 40.0
    }

    override fun postBedrockBlock(): BlockMetaPair {
        return BlockMetaPair(BLOCK_ICE, BLOCK_META_ICE)
    }
}