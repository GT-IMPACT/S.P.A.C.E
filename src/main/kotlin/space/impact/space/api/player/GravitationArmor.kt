package space.impact.space.api.player

import net.minecraft.entity.player.EntityPlayer

interface GravitationArmor {
    /**
     * Effective on worlds where the gravity is less than Overworld normal.
     *
     * @return value between 0 and 100 (other values have undefined results) 0 = standard gravity for the
     * world 50 = mid=way 100 = gravity as if the player was on the Overworld (the armor makes the player heavy)
     *
     * The total effect will be cumulative for all pieces of IArmorGravity armor worn.
     */
    fun gravityOverrideIfLow(p: EntityPlayer): Int

    /**
     * Effective on worlds where the gravity is higher than Overworld normal.
     *
     * @return value between 0 and 100 (other values have undefined results) 0 = standard gravity for the
     * world 50 = mid=way 100 = gravity as if the player was on the Overworld (the armor makes the player
     * lighter / stronger)
     *
     *
     * The total effect will be cumulative for all pieces of IArmorGravity armor worn.
     */
    fun gravityOverrideIfHigh(p: EntityPlayer): Int

}