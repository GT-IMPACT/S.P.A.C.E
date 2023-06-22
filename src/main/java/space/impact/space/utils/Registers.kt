package space.impact.space.utils

import cpw.mods.fml.common.FMLCommonHandler
import net.minecraftforge.common.MinecraftForge

object Registers {

    fun registerEvent(obj: Any) {
        FMLCommonHandler.instance().bus().register(obj)
        MinecraftForge.EVENT_BUS.register(obj)
    }

}