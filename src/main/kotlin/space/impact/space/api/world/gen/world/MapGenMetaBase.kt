package space.impact.space.api.world.gen.world

import net.minecraft.block.Block
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import java.util.*

abstract class MapGenMetaBase {

    protected var range = 8
    protected var rand = Random()
    protected var world: World? = null

    fun generate(chunk: IChunkProvider, w: World, chunkX: Int, chunkZ: Int, blocks: Array<Block?>, metadata: ByteArray) {
        world = w
        rand.setSeed(w.seed)

        val r0 = rand.nextLong()
        val r1 = rand.nextLong()

        for (x0 in chunkX - range..chunkX + range) {
            for (y0 in chunkZ - range..chunkZ + range) {
                val randX = x0 * r0
                val randZ = y0 * r1
                rand.setSeed(randX xor randZ xor w.seed)
                recursiveGenerate(w, x0, y0, chunkX, chunkZ, blocks, metadata)
            }
        }
    }

    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    protected open fun recursiveGenerate(world: World, xChunkCoord: Int, zChunkCoord: Int, origXChunkCoord: Int, origZChunkCoord: Int, blocks: Array<Block?>, metadata: ByteArray) {}
}