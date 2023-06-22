package space.impact.space.api.world.gen.layer.standart

import net.minecraft.block.Block
import space.impact.space.api.world.gen.layer.base.CreateChunkGenXZ
import space.impact.space.api.world.gen.layer.base.WorldGeneratorData
import kotlin.math.abs

class BiomeLayer : CreateChunkGenXZ() {

    private var layerBlock: ArrayList<Block> = ArrayList()
    private var layerBlockMeta: ArrayList<Byte> = ArrayList()
    private var layerReplacingBlock: ArrayList<Block?> = ArrayList()
    private var layerReplacingBlockMeta: ArrayList<Byte> = ArrayList()
    private var layerStart: ArrayList<Int> = ArrayList()
    private var layerRandomStart: ArrayList<Int> = ArrayList()
    private var layerEnd: ArrayList<Int> = ArrayList()
    private var layerRandomEnd: ArrayList<Int> = ArrayList()
    private var layerUnderWaterGen: ArrayList<Boolean> = ArrayList()


    fun add(block: Block, meta: Byte, replacingBlock: Block?, replacingBlockMeta: Byte, start: Int, rStart: Int, end: Int, rEnd: Int, underWater: Boolean) {
        layerBlock.add(block)
        layerBlockMeta.add(meta)
        layerReplacingBlock.add(replacingBlock)
        layerReplacingBlockMeta.add(replacingBlockMeta)
        layerStart.add(start)
        layerRandomStart.add(rStart)
        layerEnd.add(end)
        layerRandomEnd.add(rEnd)
        layerUnderWaterGen.add(underWater)
    }

    fun add(block: Block, meta: Byte, start: Int, rStart: Int, end: Int, rEnd: Int, underWater: Boolean) {
        layerBlock.add(block)
        layerBlockMeta.add(meta)
        layerReplacingBlock.add(null)
        layerReplacingBlockMeta.add(-1)
        layerStart.add(start)
        layerRandomStart.add(rStart)
        layerEnd.add(end)
        layerRandomEnd.add(rEnd)
        layerUnderWaterGen.add(underWater)
    }

    override fun gen(data: WorldGeneratorData) {

        for (i in layerBlock.indices) {
            var startPoint = layerStart[i]
            var endPoint = layerEnd[i]

            var y = 255

            if (layerStart[i] < 0 || layerEnd[i] < 0) {

                if (layerStart[i] < -255) {
                    startPoint = abs(layerStart[i] % 256)
                }
                if (layerEnd[i] < -255) {
                    endPoint = abs(layerEnd[i] % 256)
                }

                while (y >= 0) {
                    if (getBlock(data, y) != null) {
                        if (layerStart[i] < 0) {
                            startPoint += y
                        }
                        if (layerEnd[i] < 0) {
                            endPoint += y
                        }
                        break
                    }
                    --y
                }
            }

            if (layerRandomStart[i] > 0) {
                startPoint += data.chunkProvider.rand.nextInt(layerRandomStart[i] + 1)
            } else if (layerRandomStart[i] < 0) {
                startPoint -= data.chunkProvider.rand.nextInt(abs(layerRandomStart[i]) + 1)
            }

            if (layerRandomEnd[i] > 0) {
                endPoint += data.chunkProvider.rand.nextInt(layerRandomEnd[i] + 1)
            } else if (layerRandomEnd[i] < 0) {
                endPoint -= data.chunkProvider.rand.nextInt(abs(layerRandomEnd[i]) + 1)
            }

            if (!layerUnderWaterGen[i] && getBlock(data, startPoint + 1) != null && getBlock(data, startPoint + 1)?.material?.isLiquid == true) {
                return
            }

            if (layerReplacingBlockMeta[i].toInt() != -1) {
                y = startPoint
                while (y >= endPoint) {
                    if (getBlock(data, y) == layerReplacingBlock[i] && getBlockMeta(data, y) == layerReplacingBlockMeta[i]) {
                        setBlock(data, layerBlock[i], layerBlockMeta[i], y)
                    }
                    --y
                }
            } else {
                y = startPoint
                while (y >= endPoint) {
                    setBlock(data, layerBlock[i], layerBlockMeta[i], y)
                    --y
                }
            }
        }
    }

}