package space.impact.space.utils

import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.world.WorldProvider
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.MinecraftForge
import space.impact.space.addons.solar_system.jupiter.moons.europa.world.WorldProviderEuropa

object Registers {

    fun registerEvent(obj: Any) {
        FMLCommonHandler.instance().bus().register(obj)
        MinecraftForge.EVENT_BUS.register(obj)
    }

    fun registerPlanet(dimId: Int, providerId: Int, classProvider: Class<out WorldProvider>) {
        DimensionManager.registerProviderType(providerId, classProvider, true)
        DimensionManager.registerDimension(dimId, providerId)
    }
}