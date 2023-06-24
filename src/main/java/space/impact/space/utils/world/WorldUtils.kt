package space.impact.space.utils.world

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.FMLLog
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.play.server.S07PacketRespawn
import net.minecraft.network.play.server.S1DPacketEntityEffect
import net.minecraft.network.play.server.S1FPacketSetExperience
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import space.impact.space.api.world.damage.DamageSource
import space.impact.space.api.world.space.ISpaceSuit
import space.impact.space.api.world.world_math.Vector3

object WorldUtils {

    fun transferEntityToDimension(entity: Entity, dimensionID: Int, world: WorldServer, isFromOrbit: Boolean): Entity? {
        if (!world.isRemote) {
            val mcServer = FMLCommonHandler.instance().minecraftServerInstance
            if (mcServer != null) {
                val worldServer = mcServer.worldServerForDimension(dimensionID)
                if (worldServer == null) {
                    FMLLog.info("Cannot Transfer Entity to Dimension: Could not get World for Dimension $dimensionID")
                    return null
                }
                return teleportEntity(worldServer, entity, dimensionID, isFromOrbit)
            }
        }
        return null
    }

    fun teleportEntity(worldNew: World, entity: Entity, dimId: Int, isFromOrbit: Boolean): Entity {
        val dimChange = entity.worldObj != worldNew
        entity.worldObj.updateEntityWithOptionalForce(entity, false)
        val player: EntityPlayerMP
        val spawnPos: Vector3?
        val oldDimID = entity.worldObj.provider.dimensionId

        if (dimChange) {
            if (entity is EntityPlayerMP) {
                player = entity
                val worldOld = player.worldObj
                try {
                    (worldOld as WorldServer).playerManager.removePlayer(player)
                } catch (_: Exception) {
                }
                player.dimension = dimId
                player.playerNetServerHandler.sendPacket(
                    S07PacketRespawn(
                        dimId, player.worldObj.difficultySetting, player.worldObj.worldInfo.terrainType, player.theItemInWorldManager.gameType
                    )
                )
                removeEntityFromWorld(worldOld, player, true)
                spawnPos = Vector3(x = player.serverPosX, y = if (isFromOrbit) 400 else worldNew.getPrecipitationHeight(player.serverPosX, player.serverPosZ), z = player.serverPosZ)

                forceMoveEntityToPos(entity, worldNew as WorldServer, spawnPos, true)
                player.mcServer.configurationManager.func_72375_a(player, worldNew as WorldServer?)

                player.theItemInWorldManager.setWorld(worldNew)
                player.mcServer.configurationManager.updateTimeAndWeatherForPlayer(player, worldNew)
                player.mcServer.configurationManager.syncPlayerInventory(player)

                for (o in player.activePotionEffects) {
                    val var10 = o as PotionEffect?
                    player.playerNetServerHandler.sendPacket(S1DPacketEntityEffect(player.entityId, var10))
                }
                player.playerNetServerHandler.sendPacket(
                    S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel)
                )
            }

        } else {
            if (entity is EntityPlayerMP) {
                player = entity
                player.closeScreen()
                spawnPos = Vector3(x = player.serverPosX, y = if (isFromOrbit) 400 else worldNew.getPrecipitationHeight(player.serverPosX, player.serverPosZ), z = player.serverPosZ)
                forceMoveEntityToPos(entity, worldNew as WorldServer, spawnPos, false)

            }
        }

        if (entity is EntityPlayerMP) {
            if (dimChange) {
                FMLCommonHandler.instance().firePlayerChangedDimensionEvent(entity, oldDimID, dimId)
            }
        }
        return entity
    }

    private fun removeEntityFromWorld(var0: World, var1: Entity, directlyRemove: Boolean) {
        if (var1 is EntityPlayer) {
            var1.closeScreen()
            var0.playerEntities.remove(var1)
            var0.updateAllPlayersSleepingFlag()
        }
        if (directlyRemove) {
            val l: MutableList<Entity?> = ArrayList()
            l.add(var1)
            var0.unloadEntities(l)
        }
        var1.isDead = false
    }

    fun forceMoveEntityToPos(
        entity: Entity, worldNew: WorldServer, spawnPos: Vector3, spawnRequired: Boolean
    ) {
        val pair = worldNew.getChunkFromChunkCoords(spawnPos.x.toInt() shr 4, spawnPos.z.toInt() shr 4).chunkCoordIntPair
        worldNew.theChunkProviderServer.loadChunk(pair.chunkXPos, pair.chunkZPos)
        if (entity is EntityPlayerMP) {
            entity.playerNetServerHandler.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch)
        }
        entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch)
        if (spawnRequired) {
            worldNew.spawnEntityInWorld(entity)
            entity.setWorld(worldNew)
        }
        worldNew.updateEntityWithOptionalForce(entity, true)
    }

    fun playerBehaviorNotAtmosphere(player: EntityPlayer) {
        var countArmor = 0
        for (i in 0 until 4) {
            if (player.getCurrentArmor(i)?.item is ISpaceSuit) {
                countArmor++
            }
        }
        if (countArmor == 4) {
            //TODO
        } else {
            player.attackEntityFrom(DamageSource.openSpace, 20.0F)
        }
    }
}
