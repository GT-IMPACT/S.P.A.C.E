package space.impact.space.api.world.gen.biome

import net.minecraft.world.World

class BiomeDecoratorEmpty  : BiomeDecoratorSpaceBase() {

    private var world: World? = null

    override fun decorate() {
    }

    override fun getCurrentWorld(): World? {
        return world
    }

    override fun setCurrentWorld(world: World?) {
        this.world = world
    }
}