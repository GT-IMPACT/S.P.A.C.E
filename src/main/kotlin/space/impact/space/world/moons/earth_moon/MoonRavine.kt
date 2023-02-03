package space.impact.space.world.moons.earth_moon

import net.minecraft.init.Blocks
import space.impact.space.api.world.gen.biome.Biome
import space.impact.space.api.world.gen.layer.standart.BiomeLayer

class MoonRavine : Biome(0, false) {

    init {
        setBiomeName("MoonRavine")
        biomeMinValueOnMap = -0.05
        biomeMaxValueOnMap = 0.28
        biomePersistence = 1.2
        biomeNumberOfOctaves = 4
        biomeScaleX = 140.0
        biomeScaleY = 2.0
        biomeSurfaceHeight = 54
        biomeInterpolateQuality = 8
        this.decorateChunkGenList.clear()
        this.createChunkGenXZList.clear()
        val standardBiomeLayers = BiomeLayer()
        standardBiomeLayers.add(Blocks.packed_ice, 0, Blocks.stone, 0, -256, 0, -4, -2, true)
        standardBiomeLayers.add(Blocks.water, 0, Blocks.stone, 0, -256, 0, 0, 0, false)
        standardBiomeLayers.add(Blocks.stone, 0, Blocks.packed_ice, 0, -256, 0, -256, 0, false)
        standardBiomeLayers.add(Blocks.packed_ice, 0, 2, 4, 2, 0, true)
        standardBiomeLayers.add(Blocks.bedrock, 0, 0, 2, 0, 0, true)
        this.createChunkGenXZList.add(standardBiomeLayers)
    }
}