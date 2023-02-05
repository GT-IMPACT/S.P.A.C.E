package space.impact.space.api.events

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import space.impact.space.api.events.LightningStormHandler.spawnLightning

class EventHandler {

    @SubscribeEvent
    fun onEntityUpdate(event: LivingUpdateEvent) {
        val living = event.entityLiving

        if (living is EntityPlayer) {
            living.spawnLightning()
        }
    }

}