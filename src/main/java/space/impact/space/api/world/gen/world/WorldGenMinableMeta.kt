package space.impact.space.api.world.gen.world

import net.minecraft.block.Block
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenMinable
import java.util.*

class WorldGenMinableMeta(
    private val newBlock: Block,
    private val newMeta: Int,
    private val numberOfBlocks: Int,
    private val isUsingMeta: Boolean,
    private val oldBlock: Block,
    private val oldMeta: Int,
) : WorldGenMinable(newBlock, newMeta, numberOfBlocks, oldBlock) {

    override fun generate(w: World, rand: Random, px: Int, py: Int, pz: Int): Boolean {

        val f: Float = rand.nextFloat() * Math.PI.toFloat()

        val sinf = MathHelper.sin(f) * this.numberOfBlocks / 8.0f
        val cosf = MathHelper.cos(f) * this.numberOfBlocks / 8.0f

        val x1 = px + 8 + sinf
        val x2 = -2f * sinf

        val z1 = pz + 8 + cosf
        val z2 = -2f * cosf

        val y1: Float = (py + rand.nextInt(3) - 2).toFloat()
        val y2: Float = py + rand.nextInt(3) - 2 - y1

        for (l in 0..this.numberOfBlocks) {
            val progress = l.toFloat() / this.numberOfBlocks

            val cx = x1 + x2 * progress
            val cy = y1 + y2 * progress
            val cz = z1 + z2 * progress

            val size: Float = (((MathHelper.sin(Math.PI.toFloat() * progress) + 1.0f) * rand.nextFloat() * this.numberOfBlocks) / 16.0f + 1.0f) / 2.0f

            val xMin = MathHelper.floor_float(cx - size)
            val yMin = MathHelper.floor_float(cy - size)
            val zMin = MathHelper.floor_float(cz - size)
            val xMax = MathHelper.floor_float(cx + size)
            val yMax = MathHelper.floor_float(cy + size)
            val zMax = MathHelper.floor_float(cz + size)

            for (ix in xMin..xMax) {
                var dx = (ix + 0.5f - cx) / size
                dx *= dx
                if (dx < 1.0f) {
                    for (iy in yMin..yMax) {
                        var dy = (iy + 0.5f - cy) / size
                        dy *= dy
                        if (dx + dy < 1.0f) {
                            for (iz in zMin..zMax) {
                                var dz = (iz + 0.5f - cz) / size
                                dz *= dz
                                if (dx + dy + dz < 1.0f && w.getBlock(ix, iy, iz) == oldBlock && w.getBlockMetadata(ix, iy, iz) == oldMeta) {
                                    w.setBlock(ix, iy, iz, newBlock, if (isUsingMeta) newMeta else 0, 3)
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }
}