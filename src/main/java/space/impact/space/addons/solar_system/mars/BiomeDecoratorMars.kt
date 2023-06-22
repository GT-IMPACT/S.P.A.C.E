package space.impact.space.addons.solar_system.mars

import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.world.WorldGenMinableMeta

class BiomeDecoratorMars : BiomeDecoratorSpaceBase() {

    private var world: World? = null

    private val iceGen: WorldGenerator = WorldGenMinableMeta(Blocks.ice, 0, 18, true, SolarSystem.MARS_BLOCKS, 0)

    override fun decorate() {
        this.generateOther(4, iceGen, 60, 120)
    }

    override fun getCurrentWorld(): World? {
        return world
    }

    override fun setCurrentWorld(world: World?) {
        this.world = world
    }
}