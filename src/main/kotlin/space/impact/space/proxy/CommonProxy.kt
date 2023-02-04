package space.impact.space.proxy

import cpw.mods.fml.common.event.*
import net.minecraftforge.common.DimensionManager
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.addons.solar_system.jupiter.moons.europa.world.WorldProviderEuropa
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.client.effects.EffectData
import space.impact.space.command.DTPCommand
import space.impact.space.config.Config

open class CommonProxy {

    open fun preInit(event: FMLPreInitializationEvent) {
        Config.createConfig(event.modConfigurationDirectory)
    }

    open fun init(event: FMLInitializationEvent) {
        SolarSystem.register()
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

    open fun spawnParticle(position: Vector3, motion: Vector3, effectData: EffectData) {}
}