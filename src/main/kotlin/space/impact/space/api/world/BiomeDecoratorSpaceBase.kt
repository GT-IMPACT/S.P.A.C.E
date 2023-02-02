package space.impact.space.api.world

import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import net.minecraftforge.common.MinecraftForge
import space.impact.space.api.world.event.EventPopulate
import java.util.*

abstract class BiomeDecoratorSpaceBase {

    protected var rand: Random? = null

    protected var chunkX = 0
    protected var chunkZ = 0

    fun decorate(world: World?, random: Random?, chunkX: Int, chunkZ: Int) {
        if (getCurrentWorld() != null) {
            throw RuntimeException("Already decorating!!")
        } else {
            setCurrentWorld(world)
            rand = random
            this.chunkX = chunkX
            this.chunkZ = chunkZ
            MinecraftForge.EVENT_BUS.post(EventPopulate.Pre(world, rand, this.chunkX, this.chunkZ))
            this.decorate()
            MinecraftForge.EVENT_BUS.post(EventPopulate. Post(world, rand, this.chunkX, this.chunkZ))
            setCurrentWorld(null)
            rand = null
        }
    }

    protected abstract fun getCurrentWorld(): World?

    protected abstract fun setCurrentWorld(world: World?)

    protected fun generateOther(amountPerChunk: Int, worldGenerator: WorldGenerator, minY: Int, maxY: Int) {
        val currentWorld = getCurrentWorld()
        for (var5 in 0 until amountPerChunk) {
            val var6 = chunkX + (rand?.nextInt(16) ?: 0)
            val var7 = (rand?.nextInt(maxY - minY) ?: 0) + minY
            val var8 = chunkZ + (rand?.nextInt(16) ?: 0)
            worldGenerator.generate(currentWorld, rand, var6, var7, var8)
        }
    }

    protected abstract fun decorate()

}