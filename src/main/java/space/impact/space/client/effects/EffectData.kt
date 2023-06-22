package space.impact.space.client.effects

import space.impact.space.api.world.world_math.Vector3

sealed class EffectData {
    class IceWaterEffect(val age: Int, val particleId: Int, val isGravity: Boolean, val color: Vector3?, val size: Double) : EffectData()
}
