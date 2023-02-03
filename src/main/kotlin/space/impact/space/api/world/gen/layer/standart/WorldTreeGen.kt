package space.impact.space.api.world.gen.layer.standart

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.block.Block
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import space.impact.space.api.world.gen.layer.standart.help.TreeGen
import java.util.*

class WorldTreeGen : IWorldGenerator {

    private var ug: ArrayList<TreeGen> = ArrayList()
    private var cwt: ArrayList<Int> = ArrayList()
    private var tfc: ArrayList<Int> = ArrayList()
    private var tfb: ArrayList<Int> = ArrayList()

    fun add(
        bWood: Block, mWood: Int, bLeaves: Block,
        mLeaves: Int, bSapling: Block, bVine: Block, bCocoa: Block,
        v1: Int, v2: Int, v3: Int,
        minHeight: Int, vines: Boolean,
        ocp1: Byte, ocp2: Byte, ocp3: Byte, ocp4: Byte, ocp5: Byte, ocp6: Byte,
        ts: Int, hl: Int, ldl: Int,
        ha: Double, bs: Double, sw: Double, ld: Double
    ) {
//        ug.add(TreeGen(false))
//        this.bg.add(BigTreeGen(false))
//        cwt.add(v1)
//        tfc.add(v2)
//        tfb.add(v3)
//        (ug[ug.size - 1]).bWood = bWood
//        (ug[ug.size - 1]).metaWood = mWood
//        (ug[ug.size - 1]).bLeaves = bLeaves
//        (ug[ug.size - 1]).metaLeaves = mLeaves
//        (ug[ug.size - 1]).bSapling = bSapling
//        (ug[ug.size - 1]).bVine = bVine
//        (ug[ug.size - 1]).bCocoa = bCocoa
//        (ug[ug.size - 1]).minTreeHeight = minHeight
//        (ug[ug.size - 1]).vinesGrow = vines
//
//        (this.bg.get(this.bg.size - 1)).bWood = bWood
//        (this.bg.get(this.bg.size - 1)).bLeaves = bLeaves
//        (this.bg.get(this.bg.size - 1)).metaLeaves = mLeaves
//        (this.bg.get(this.bg.size - 1)).bSapling = bSapling
//
//        (this.bg.get(this.bg.size - 1)).otherCoordPairs = byteArrayOf(ocp1, ocp2, ocp3, ocp4, ocp5, ocp6)
//        (this.bg.get(this.bg.size - 1)).trunkSize = ts
//        (this.bg.get(this.bg.size - 1)).heightLimitLimit = hl
//        (this.bg.get(this.bg.size - 1)).leafDistanceLimit = ldl
//        (this.bg.get(this.bg.size - 1)).heightAttenuation = ha
//        (this.bg.get(this.bg.size - 1)).branchSlope = bs
//        (this.bg.get(this.bg.size - 1)).scaleWidth = sw
//        (this.bg.get(this.bg.size - 1)).leafDensity = ld

        add(bWood, mWood, bLeaves, mLeaves, bSapling, bVine, bCocoa, v1, v2, minHeight, vines)
    }

    fun add(
        bWood: Block, mWood: Int, bLeaves: Block, mLeaves: Int,
        bSapling: Block, bVine: Block, bCocoa: Block,
        v1: Int, v2: Int, minHeight: Int, vines: Boolean
    ) {
        ug.add(TreeGen(false))
        cwt.add(v1)
        tfc.add(v2)
        tfb.add(0)
        (ug[ug.size - 1]).bWood = bWood
        (ug[ug.size - 1]).metaWood = mWood
        (ug[ug.size - 1]).bLeaves = bLeaves
        (ug[ug.size - 1]).metaLeaves = mLeaves
        (ug[ug.size - 1]).bSapling = bSapling
        (ug[ug.size - 1]).bVine = bVine
        (ug[ug.size - 1]).bCocoa = bCocoa
        (ug[ug.size - 1]).minTreeHeight = minHeight
        (ug[ug.size - 1]).vinesGrow = vines
    }

    override fun generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
        for (i in ug.indices) {
            if (random.nextInt(cwt[i]) == 0) {
                for (i2 in 0 until tfc[i]) {
                    val x = chunkX * 16 + random.nextInt(16)
                    val z = chunkZ * 16 + random.nextInt(16)
                    val y = world.getHeightValue(x, z)
                    if (tfb[i] <= 0) {
                        ug[i].generate(world, random, x, y, z)
                    } else if (random.nextInt(tfb[i]) == 0) {
//                        (this.bg.get(i) as WE_BigTreeGen).generate(world, random, x, y, z)
                    } else {
                        ug[i].generate(world, random, x, y, z)
                    }
                }
            }
        }
    }

}