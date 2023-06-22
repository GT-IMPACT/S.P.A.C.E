package space.impact.space.api.events

import cpw.mods.fml.common.eventhandler.Cancelable
import net.minecraft.entity.Entity
import net.minecraftforge.event.entity.EntityEvent

@Cancelable
class TeleportEvent(val entity: Entity?, val x: Int, val y: Int, val z: Int, val dim: Int) : EntityEvent(entity)