package space.impact.space.utils.ext

import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import space.impact.space.api.player.GravitationArmor
import space.impact.space.api.world.gen.world.SpaceProvider

object Gravitation {

    private const val DEFAULT_GRAVITY = 0.08

    @JvmStatic
    fun setGravityEntity(e: Entity): Double {
        val provider = e.worldObj.provider
         if (provider is SpaceProvider) {
            if (e is EntityPlayer) {
                if (e.inventory != null) {

                    var armorModLowGrav = 100
                    var armorModHighGrav = 100

                    for (slot in 0..3) {
                        val armor = e.getCurrentArmor(slot)?.item
                        if (armor is GravitationArmor) {
                            armorModLowGrav = armor.gravityOverrideIfLow(e)
                            armorModHighGrav = armor.gravityOverrideIfHigh(e)
                        }
                    }

                    if (armorModLowGrav > 100) {
                        armorModLowGrav = 100
                    }
                    if (armorModHighGrav > 100) {
                        armorModHighGrav = 100
                    }
                    if (armorModLowGrav < 0) {
                        armorModLowGrav = 0
                    }
                    if (armorModHighGrav < 0) {
                        armorModHighGrav = 0
                    }

                    if (provider.getGravitationMultiply() > 0) {
                        return DEFAULT_GRAVITY - provider.getGravitationMultiply() * armorModLowGrav / 100
                    }
                    return DEFAULT_GRAVITY - provider.getGravitationMultiply() * armorModHighGrav / 100
                }
            }
            return DEFAULT_GRAVITY - provider.getGravitationMultiply()
        }
        return DEFAULT_GRAVITY
    }


    @JvmStatic
    fun setItemGravity(e: EntityItem): Double {
        return if (e.worldObj.provider is SpaceProvider) {
            val customProvider: SpaceProvider = e.worldObj.provider as SpaceProvider
            0.002.coerceAtLeast(0.039 - customProvider.getGravitationMultiply() / 1.75)
        } else {
            0.039
        }
    }
}