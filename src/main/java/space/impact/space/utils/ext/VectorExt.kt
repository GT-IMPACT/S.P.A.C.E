package space.impact.space.utils.ext

import space.impact.space.api.world.world_math.Vector3

fun Number.toVector(): Vector3 = Vector3(this, this, this)

