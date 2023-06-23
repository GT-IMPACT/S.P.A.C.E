package space.impact.space.api.events

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.util.ChunkCoordinates
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.living.LivingFallEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.BlockEvent
import space.impact.space.api.events.LightningStormHandler.spawnLightning
import space.impact.space.api.world.gen.world.SpaceProvider
import space.impact.space.config.Config

class EventHandler {

    companion object {
        var bedActivated = false
    }

    @SubscribeEvent
    fun onEntityUpdate(event: LivingUpdateEvent) {
        val living = event.entityLiving

        if (living is EntityPlayer) {
            living.spawnLightning()
        }
    }

    @SubscribeEvent
    fun onEntityFall(event: LivingFallEvent) {
        val provider = event.entityLiving.worldObj.provider
        if (provider is SpaceProvider) {
            event.distance *= provider.getFallDamageModifier()
        }
    }

    @SubscribeEvent
    fun onPlayerClicked(event: PlayerInteractEvent) {
        if (event.entityPlayer == null || event.entityPlayer.inventory == null) {
            return
        }

        val world = event.entityPlayer.worldObj ?: return

        val idClicked: Block = world.getBlock(event.x, event.y, event.z)

        if (idClicked == Blocks.bed && world.provider is SpaceProvider && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && !world.isRemote) {

            bedActivated = true;
            if (world.provider.canRespawnHere() && !bedActivated) {
                bedActivated = true
                event.entityPlayer.setSpawnChunk(ChunkCoordinates(event.x, event.y, event.z), false)
            } else {
                bedActivated = false
            }
        }
    }

    @SubscribeEvent
    fun onPlaceBlock(event: BlockEvent.PlaceEvent) {
        if (event.world.provider.dimensionId > 0) {
            for (block in Config.blocksDisabledPlaceWorld) {
                if (event.placedBlock == block) {
                    event.isCanceled = true
                }
            }
        }
    }
}