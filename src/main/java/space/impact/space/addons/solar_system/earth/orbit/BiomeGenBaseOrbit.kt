package space.impact.space.addons.solar_system.earth.orbit

import net.minecraft.world.biome.BiomeGenBase

class BiomeGenBaseOrbit private constructor(var1: Int) : BiomeGenBase(var1) {
    init {
        spawnableMonsterList.clear()
        spawnableWaterCreatureList.clear()
        spawnableCreatureList.clear()
        rainfall = 0f
    }

    override fun setColor(var1: Int): BiomeGenBase {
        return super.setColor(var1)
    }

    override fun getSpawningChance(): Float {
        return 0.01f
    }

    companion object {
        val space: BiomeGenBase = BiomeGenBaseOrbit(255)
            .setBiomeName("space")
    }
}