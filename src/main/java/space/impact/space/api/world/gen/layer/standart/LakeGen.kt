package space.impact.space.api.world.gen.layer.standart

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import java.util.*

class LakeGen : IWorldGenerator {

    private var lakeBlock = Blocks.water
    private var lakeBlockF = Blocks.ice
    private var lakeBlockMeta = 0
    private var lakeBlockMetaF = 0
    private var chunksForLake = 12
    private var minY = 0
    private var maxY = 255
    private var fY = 111
    private var randomFY = 2
    private var isFGen = true
    private var isU = true

    override fun generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
        if (random.nextInt(chunksForLake) == 0) {
            val x = chunkX * 16 + random.nextInt(16)
            val z = chunkZ * 16 + random.nextInt(16)
            val y = minY + random.nextInt(maxY - minY + 1)
            this.lake(world, random, x, y, z)
        }
    }

    private fun lake(world: World, random: Random, xx: Int, yy: Int, zz: Int) {
        var x = xx
        var y = yy
        var z = zz
        x -= 8

        z -= 8
        while (y > 5 && world.isAirBlock(x, y, z)) {
            --y
        }

        if (y > 4) {
            y -= 4
            val isAccess = BooleanArray(2048)
            var bx = 0
            bx = 0
            while (bx < random.nextInt(4) + 4) {
                val d0 = random.nextDouble() * 6.0 + 3.0
                val d1 = random.nextDouble() * 4.0 + 2.0
                val d2 = random.nextDouble() * 6.0 + 3.0
                val d3 = random.nextDouble() * (14.0 - d0) + 1.0 + d0 / 2.0
                val d4 = random.nextDouble() * (4.0 - d1) + 2.0 + d1 / 2.0
                val d5 = random.nextDouble() * (14.0 - d2) + 1.0 + d2 / 2.0
                for (bx in 1..14) {
                    for (bz in 1..14) {
                        for (by in 1..6) {
                            val d6 = (bx.toDouble() - d3) * 2.0 / d0
                            val d7 = (by.toDouble() - d4) * 2.0 / d1
                            val d8 = (bz.toDouble() - d5) * 2.0 / d2
                            val d9 = d6 * d6 + d7 * d7 + d8 * d8
                            if (d9 < 1.0) {
                                isAccess[(bx * 16 + bz) * 8 + by] = true
                            }
                        }
                    }
                }
                ++bx
            }

            bx = 0
            while (bx < 16) {
                for (bz in 0..15) {
                    for (by in 0..7) {

                        val b1 = isAccess[(bx * 16 + bz) * 8 + by]
                        val b2 = bx < 15 && isAccess[((bx + 1) * 16 + bz) * 8 + by]
                        val b3 = bx > 0 && isAccess[((bx - 1) * 16 + bz) * 8 + by]
                        val b4 = bz < 15 && isAccess[(bx * 16 + bz + 1) * 8 + by]
                        val b5 = bz > 0 && isAccess[(bx * 16 + bz - 1) * 8 + by]
                        val b6 = by < 7 && isAccess[(bx * 16 + bz) * 8 + by + 1]
                        val b7 = by > 0 && isAccess[(bx * 16 + bz) * 8 + by - 1]

                        if (!b1 && (b2 || b3 || b4 || b5 || b6 || b7)) {
                            val material = world.getBlock(x + bx, y + by, z + bz).material
                            if (by >= 4 && material.isLiquid) {
                                return
                            }
                            if (by < 4 && !material.isSolid && (world.getBlock(x + bx, y + by, z + bz) != lakeBlock ||
                                        world.getBlockMetadata(x + bx, y + by, z + bz) != lakeBlockMeta)
                            ) {
                                return
                            }
                        }
                        if (isAccess[(bx * 16 + bz) * 8 + by]) {
                            world.setBlock(x + bx, y + by, z + bz, if (by >= 4) Blocks.air else lakeBlock, lakeBlockMeta, if (isU) 3 else 2)
                        }
                    }
                    if (y + 4 >= fY + random.nextInt(this.randomFY + 1)) {
                        world.setBlock(x + bx, y + 4, z + bz, this.lakeBlockF, this.lakeBlockMetaF, if (isU) 3 else 2)
                    }
                }
                ++bx
            }
        }
    }
}