package space.impact.space.api.world.gen.layer.standart

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.block.Block
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import java.util.*

class GrassGen : IWorldGenerator {

    var flowerList: ArrayList<Block> = ArrayList()
    var flowerMetaList: ArrayList<Byte> = ArrayList()
    var blocksForFlower: ArrayList<Int> = ArrayList()
    var waterGen: ArrayList<Boolean> = ArrayList()
    var surface: ArrayList<Block> = ArrayList()
    var surfaceMeta: ArrayList<Byte> = ArrayList()

    fun add(f: Block, fm: Byte, bff: Int, wg: Boolean, s: Block, sm: Byte) {
        flowerList.add(f)
        flowerMetaList.add(fm)
        blocksForFlower.add(bff)
        waterGen.add(wg)
        surface.add(s)
        surfaceMeta.add(sm)
    }

    override fun generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider?, chunkProvider: IChunkProvider?) {
        for (i in flowerList.indices) {
            if (random.nextInt(blocksForFlower[i]) <= 255) {
                for (i2 in 0 until MathHelper.ceiling_float_int(256.0f / blocksForFlower[i].toFloat())) {
                    val x = chunkX * 16 + random.nextInt(16)
                    val z = chunkZ * 16 + random.nextInt(16)
                    var y = world.getHeightValue(x, z)
                    if (!waterGen[i]) {
                        if (world.getBlock(x, y - 1, z) == surface[i] && world.getBlockMetadata(x, y - 1, z) == surfaceMeta[i].toInt()) {
                            world.setBlock(x, y, z, flowerList[i], flowerMetaList[i].toInt(), 2)
                        }
                    } else if (world.getBlock(x, y - 1, z).material.isLiquid) {
                        --y
                        while (world.getBlock(x, y - 1, z).material.isLiquid) {
                            --y
                        }
                        if (world.getBlock(x, y - 1, z) == surface[i] && world.getBlockMetadata(x, y - 1, z) == surfaceMeta[i].toInt()) {
                            world.setBlock(x, y, z, flowerList[i], flowerMetaList[i].toInt(), 2)
                        }
                    }
                }
            }
        }
    }

}