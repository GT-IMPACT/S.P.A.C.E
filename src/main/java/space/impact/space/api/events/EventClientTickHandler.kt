package space.impact.space.api.events

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import space.impact.space.addons.solar_system.venus.world.CloudProviderVenus
import space.impact.space.addons.solar_system.venus.world.WorldProviderVenus

class EventClientTickHandler {

    private var mc: Minecraft = FMLClientHandler.instance().client

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onClientTick(e: ClientTickEvent) {
        val world = mc.theWorld

        if (e.phase == TickEvent.Phase.START) {
            if (!mc.isGamePaused) {
                CloudProviderVenus.cloudTickCounter += 3
            }
        }

        if (world != null && world.provider is WorldProviderVenus && world.worldInfo.isRaining) {
            world.setRainStrength(1.5f)
        }


    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onRenderTick(event: RenderTickEvent) {

    }

}