package space.impact.space.addons.solar_system.venus.world

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.chunk.IChunkProvider
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.biome.SpaceBiomeGenBase
import space.impact.space.api.world.gen.chunk.ChunkProviderSpaceLakes
import space.impact.space.api.world.gen.other.BlockMetaPair
import space.impact.space.api.world.gen.world.MapGenMetaBase

class ChunkProviderVenus(world: World, seed: Long, flag: Boolean) : ChunkProviderSpaceLakes(world, seed, flag) {

    companion object {
        private val BLOCK_GRUNT = BlockMetaPair(SolarSystem.VENUS_BLOCKS, 0)
        private val BLOCK_STONE = BlockMetaPair(SolarSystem.VENUS_BLOCKS, 1)
        private val BLOCK_DEEP_STONE = BlockMetaPair(SolarSystem.VENUS_BLOCKS, 1)
    }

    override var generatedBiomes: Array<BiomeGenBase> = getBiomesForGeneration()
    private val caveGenerator = MapGenCavesVenus()

    override fun getWorldGenerators(): List<MapGenMetaBase> {
        return listOf(caveGenerator)
    }

    override fun getBiomeGenerator(): BiomeDecoratorSpaceBase {
        return BiomeDecoratorVenus()
    }

    override fun getBiomesForGeneration(): Array<BiomeGenBase> {
        return arrayOf(
            SpaceBiomeGenBase.SPACE_LOW_HILLS.setBiomeName("VenusLowHills"),
            SpaceBiomeGenBase.SPACE_LOW_PLAINS.setBiomeName("VenusLowPlains"),
            SpaceBiomeGenBase.SPACE_SHALLOW_WATERS.setBiomeName("VenusShallowWaters"),
        )
    }

    override fun getCraterProbability(): Int {
        return 0
    }

    override fun getCreatures(): Array<BiomeGenBase.SpawnListEntry?> {
        return arrayOfNulls(0)
    }

    override fun getHeightModifier(): Double {
        return 50.0
    }

    override fun getMonsters(): Array<BiomeGenBase.SpawnListEntry?> {
        return arrayOf()
    }

    override fun getMountainHeightModifier(): Double {
        return 40.0
    }

    override fun getSmallFeatureHeightModifier(): Double {
        return 5.0
    }

    override fun getValleyHeightModifier(): Double {
        return 50.0
    }

    override fun onChunkProvider(cX: Int, cZ: Int, blocks: Array<Block?>, metadata: ByteArray) {}

    override fun onPopulate(provider: IChunkProvider?, chX: Int, chZ: Int) {
    }

    override fun getWaterCreatures(): Array<BiomeGenBase.SpawnListEntry?> {
        return arrayOfNulls(0)
    }

    override fun getGrassBlock(): BlockMetaPair {
        return BLOCK_GRUNT
    }

    override fun getDirtBlock(): BlockMetaPair {
        return BLOCK_STONE
    }

    override fun getStoneBlock(): BlockMetaPair {
        return BLOCK_DEEP_STONE
    }

    override fun enableBiomeGenBaseBlock(): Boolean {
        return false
    }

    override fun getTypeGen(): Int {
        return GEN_TYPE_LAND_DEFAULT
    }

    override fun getWaterLevel(): Int {
        return 60
    }

    override fun canGenerateWaterBlock(): Boolean {
        return false
    }

    override fun canGeneratePostBedrock(): Boolean {
        return false
    }

    override fun getWaterBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.lava, 0)
    }

    override fun postBedrockBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.air, 0)
    }

    override fun chunkExists(x: Int, z: Int): Boolean {
        return false
    }
}