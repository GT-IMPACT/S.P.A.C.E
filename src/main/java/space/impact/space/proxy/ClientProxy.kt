package space.impact.space.proxy

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import space.impact.space.api.events.EventClientTickHandler
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.client.effects.EffectData
import space.impact.space.client.effects.EffectHandler
import space.impact.space.utils.Registers.registerEvent

class ClientProxy : CommonProxy() {

    override fun spawnParticle(position: Vector3, motion: Vector3, effectData: EffectData) {
        EffectHandler.spawnParticle(position, motion, effectData)
    }

}