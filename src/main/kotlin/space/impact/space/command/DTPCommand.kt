package space.impact.space.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.MinecraftForge
import space.impact.space.events.TeleportEvent


class DTPCommand : CommandBase() {

    override fun getRequiredPermissionLevel(): Int {
        return 4
    }
    override fun getCommandName(): String? {
        return "dtp"
    }

    override fun processCommand(ics: ICommandSender?, args: Array<String>) {
        if (ics !is EntityPlayer) {
            return
        }
        val x = args[0].toInt()
        val y = args[1].toInt()
        val z = args[2].toInt()
        val d = args[3].toInt()
        val tEvent = TeleportEvent(ics as? Entity, x, y, z, d)
        MinecraftForge.EVENT_BUS.post(tEvent)
        if (!tEvent.isCanceled) {
            moveEntity(ics as Entity?, d, x + 0.5, y + 0.5, z + 0.5)
        }
    }

    override fun getCommandUsage(sender: ICommandSender?): String? {
        return "dimension teleport /dtp <X> <Y> <Z> <D>"
    }

    private fun moveEntity(e: Entity?, dim: Int, x: Double, y: Double, z: Double) {
        if (e == null) return
        val targetWorld = DimensionManager.getWorld(dim)
        val currentWorld = DimensionManager.getWorld(e.worldObj.provider.dimensionId)

        if (targetWorld != null && currentWorld != null && targetWorld != currentWorld) {
            if (e.ridingEntity != null) e.mountEntity(null)

            if (e.riddenByEntity != null) e.riddenByEntity.mountEntity(null);

            if (e is EntityPlayerMP) {
                e.travelToDimension(dim)
                e.playerNetServerHandler.setPlayerLocation(x + .5, y + .5, z + .5, e.rotationYaw, e.rotationPitch)
                e.setPositionAndUpdate(x, y, z)
            }
            currentWorld.resetUpdateEntityTick()
            targetWorld.resetUpdateEntityTick()
        }
    }
}