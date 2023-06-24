package space.impact.space.api.world.damage

import net.minecraft.util.DamageSource

object DamageSource {
    val openSpace: DamageSource = DamageSource("openSpace").setDamageBypassesArmor()
}