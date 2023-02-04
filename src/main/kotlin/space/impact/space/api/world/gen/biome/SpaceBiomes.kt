package space.impact.space.api.world.gen.biome

import net.minecraft.world.biome.BiomeGenBase

class SpaceBiomeGenBase(id: Int) : BiomeGenBase(id) {

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

    override fun getSpawningChance() = 0.7f
}

object SpaceBiomes {

    val SPACE = SpaceBiomeGenBase(228)
        .setBiomeName("S.P.A.C.E")
        .setHeight(BiomeGenBase.Height(0.1F, 0.2F))

}