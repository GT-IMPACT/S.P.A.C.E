package space.impact.space.addons.solar_system.mars

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

class ChunkProviderMars(world: World, seed: Long, flag: Boolean) : ChunkProviderSpaceLakes(world, seed, flag) {

    companion object {
        private val BLOCK_GRUNT = BlockMetaPair(SolarSystem.MARS_BLOCKS, 0)
        private val BLOCK_STONE = BlockMetaPair(SolarSystem.MARS_BLOCKS, 1)
        private val BLOCK_DEEP_STONE = BlockMetaPair(SolarSystem.MARS_BLOCKS, 2)
    }

    override var generatedBiomes: Array<BiomeGenBase> = getBiomesForGeneration()
    private val caveGenerator = MapGenCaveMars()

    override fun getWorldGenerators(): List<MapGenMetaBase> {
        return listOf(caveGenerator)
    }

    override fun getBiomeGenerator(): BiomeDecoratorSpaceBase {
        return BiomeDecoratorMars()
    }

    override fun getBiomesForGeneration(): Array<BiomeGenBase> {
        return arrayOf(SpaceBiomeGenBase.SPACE.setBiomeName("Mars"))
    }

    override fun getCraterProbability(): Int {
        return 2000
    }

    override fun getCreatures(): Array<BiomeGenBase.SpawnListEntry?> {
        return arrayOfNulls(0)
    }

    override fun getHeightModifier(): Double {
        return 12.0
    }

    override fun getMonsters(): Array<BiomeGenBase.SpawnListEntry?> {
        return arrayOf()
    }

    override fun getMountainHeightModifier(): Double {
        return 95.0
    }

    override fun getSmallFeatureHeightModifier(): Double {
        return 26.0
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
        return 95
    }

    override fun canGenerateWaterBlock(): Boolean {
        return false
    }

    override fun canGeneratePostBedrock(): Boolean {
        return false
    }

    override fun getWaterBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.air, 0)
    }

    override fun postBedrockBlock(): BlockMetaPair {
        return BlockMetaPair(Blocks.air, 0)
    }

    override fun chunkExists(x: Int, z: Int): Boolean {
        return false
    }
}