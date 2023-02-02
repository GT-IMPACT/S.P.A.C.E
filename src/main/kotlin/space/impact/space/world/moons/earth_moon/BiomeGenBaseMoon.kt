package space.impact.space.world.moons.earth_moon

import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.common.BiomeDictionary

class BiomeGenBaseMoon(id: Int) : BiomeGenBase(id) {

    companion object {
        val moonFlat: BiomeGenBase = BiomeGenBaseMoon(2).apply {
            setBiomeName("moonFlat")
            setColor(11111111)
            setHeight(Height(1.5f, 0.4f))
            BiomeDictionary.registerBiomeType(
                this,
                BiomeDictionary.Type.COLD,
                BiomeDictionary.Type.DRY,
                BiomeDictionary.Type.DEAD
            )
        }
    }

    init {
        spawnableMonsterList.clear()
        spawnableWaterCreatureList.clear()
        spawnableCreatureList.clear()
        rainfall = 0f
    }

    override fun setColor(color: Int): BiomeGenBaseMoon {
        return super.setColor(color) as BiomeGenBaseMoon
    }

    override fun getSpawningChance(): Float {
        return 0.0f
    }
}