package space.impact.space.api.world.gen.layer.standart

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import space.impact.space.api.world.gen.layer.base.CreateChunkGenXZ
import space.impact.space.api.world.gen.layer.base.WorldGeneratorData

class SnowGen : CreateChunkGenXZ() {

    private var snowPoint = 111
    private var randomSnowPoint = 2
    private var snowBlock: Block = Blocks.snow_layer
    private var snowBlockMeta: Byte = 0
    private var genSnow = true
    private var iceBlock: Block = Blocks.ice
    private var iceBlockMeta: Byte = 0
    private var freezeMaterial: Material = Material.water
    private var snowOnWaterRandom = 2

    override fun gen(data: WorldGeneratorData) {
        for (y in 255 downTo snowPoint + data.chunkProvider.rand.nextInt(randomSnowPoint + 1)) {
            if (getBlock(data, y) != null) {
                if (getBlock(data, y)!!.material == freezeMaterial) {
                    setBlock(data, iceBlock, iceBlockMeta, y)
                    if (genSnow && data.chunkProvider.rand.nextInt(snowOnWaterRandom + 1) == 0) {
                        setBlock(data, snowBlock, snowBlockMeta, y + 1)
                    }
                } else if (genSnow) {
                    setBlock(data, snowBlock, snowBlockMeta, y + 1)
                }
                break
            }
        }
    }
}