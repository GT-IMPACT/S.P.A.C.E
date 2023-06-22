package space.impact.space.api.world.gen.biome

import net.minecraft.world.biome.BiomeGenBase

class SpaceBiomeGenBase(id: Int) : BiomeGenBase(id) {

    companion object {
        val SPACE: BiomeGenBase = SpaceBiomeGenBase(200)
            .setBiomeName("S.P.A.C.E")
            .setHeight(height_Default)

        val SPACE_LOW_HILLS: BiomeGenBase = SpaceBiomeGenBase(201)
            .setBiomeName("SpaceLowHills")
            .setHeight(height_LowHills)

        val SPACE_LOW_PLAINS: BiomeGenBase = SpaceBiomeGenBase(202)
            .setBiomeName("SpaceLowHills")
            .setHeight(height_LowPlains)

        val SPACE_SHALLOW_WATERS: BiomeGenBase = SpaceBiomeGenBase(202)
            .setBiomeName("SpaceShallowWaters")
            .setHeight(height_ShallowWaters)
    }

    init {
        spawnableMonsterList.clear()
        spawnableWaterCreatureList.clear()
        spawnableCreatureList.clear()
        spawnableCaveCreatureList.clear()
        rainfall = 0.0f
        setColor(-16744448)
        theBiomeDecorator.treesPerChunk = -999
        theBiomeDecorator.flowersPerChunk = -999
        theBiomeDecorator.grassPerChunk = -999
    }

    override fun getSpawningChance() = 0.1f
}
