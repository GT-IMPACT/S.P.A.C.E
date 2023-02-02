package space.impact.space.proxy

import cpw.mods.fml.common.event.*
import net.minecraftforge.common.DimensionManager
import space.impact.space.command.DTPCommand
import space.impact.space.config.Config
import space.impact.space.world.moons.earth_moon.WorldProviderMoon

open class CommonProxy {

    open fun preInit(event: FMLPreInitializationEvent) {
        Config.createConfig(event.modConfigurationDirectory)
    }

    open fun init(event: FMLInitializationEvent) {
        DimensionManager.registerProviderType(2, WorldProviderMoon::class.java, true)
        DimensionManager.registerDimension(2, 2)
    }

    open fun postInit(event: FMLPostInitializationEvent) {
    }

    open fun serverAboutToStart(event: FMLServerAboutToStartEvent) {
    }

    open fun serverStarting(event: FMLServerStartingEvent) {
        event.registerServerCommand(DTPCommand())
    }

    open fun serverStarted(event: FMLServerStartedEvent) {
    }

    open fun serverStopping(event: FMLServerStoppingEvent) {
    }

    open fun serverStopped(event: FMLServerStoppedEvent) {
    }
}