package space.impact.space.client.effects

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.Entity
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.util.Vec3
import net.minecraft.world.World
import space.impact.space.api.world.world_math.Vector3

@SideOnly(Side.CLIENT)
class EntityIceWater(
    val world: World, pos: Vector3, motion: Vector3, age: Int,
    particleId: Int, private val isGravity: Boolean, color: Vector3?, size: Double,
) : EntityFX(world, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0) {

    private var flameScale = 0f

    init {
        motionX = motionX * 0.009999999776482582 + motion.x
        motionY = motionY * 0.009999999776482582 + motion.y
        motionZ = motionZ * 0.009999999776482582 + motion.z
        particleScale = (particleScale.toDouble() * 5.0 * size).toFloat()
        flameScale = particleScale
        particleRed = 1.0f.also { particleBlue = it }.also { particleGreen = it }
        particleMaxAge = (8.0 / (Math.random() * 0.8 + 0.2)).toInt() + age
        setParticleTextureIndex(particleId)
        if (color == null) {
            particleBlue = 1.0f
            particleGreen = particleBlue
            particleRed = particleGreen
        } else {
            particleRed = color.x.toFloat()
            particleGreen = color.y.toFloat()
            particleBlue = color.x.toFloat()
        }
    }

    override fun renderParticle(par1Tessellator: Tessellator?, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, par7: Float) {
        val var8 = (super.particleAge.toFloat() + par2) / super.particleMaxAge.toFloat()
        super.particleScale = flameScale * (0.5f - var8 * var8 * 0.5f)
        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7)
    }

    override fun getBrightnessForRender(par1: Float): Int {
        var var2 = (super.particleAge.toFloat() + par1) / super.particleMaxAge.toFloat()
        if (var2 < 0.0f) {
            var2 = 0.0f
        }
        if (var2 > 1.0f) {
            var2 = 1.0f
        }
        val var3 = super.getBrightnessForRender(par1)
        var var4 = var3 and 255
        val var5 = var3 shr 16 and 255
        var4 += (var2 * 15.0f * 16.0f).toInt()
        if (var4 > 240) {
            var4 = 240
        }
        return var4 or (var5 shl 16)
    }

    override fun getBrightness(par1: Float): Float {
        var var2 = (super.particleAge.toFloat() + par1) / super.particleMaxAge.toFloat()
        if (var2 < 0.0f) {
            var2 = 0.0f
        }
        if (var2 > 1.0f) {
            var2 = 1.0f
        }
        val var3 = super.getBrightness(par1)
        return var3 * var2 + (1.0f - var2)
    }

    override fun onUpdate() {
        super.prevPosX = super.posX
        super.prevPosY = super.posY
        super.prevPosZ = super.posZ
        if (super.particleAge++ >= super.particleMaxAge) {
            setDead()
        }
        if (super.particleAge > super.particleMaxAge / 10 && isGravity) {
            super.motionY -= 0.04
        }
        super.motionY += 0.01
        moveEntity(super.motionX, super.motionY, super.motionZ)
        super.motionX *= 0.9599999785423279
        super.motionY *= 0.9599999785423279
        super.motionZ *= 0.9599999785423279
        if (super.onGround) {
            super.motionX *= 0.699999988079071
            super.motionZ *= 0.699999988079071
        }
        val vec31 = Vec3.createVectorHelper(posX, posY, posZ)
        var vec3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ)
        var movingobjectposition = worldObj.func_147447_a(vec31, vec3, false, true, false)
        if (movingobjectposition != null) {
            vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord)
        }
        var entity: Entity? = null
        val list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0))
        var d0 = 0.0
        for (i in list.indices) {
            val entity1 = list[i] as Entity
            if (entity1.canBeCollidedWith()) {
                val f1 = 0.3f
                val axisalignedbb1 = entity1.boundingBox.expand(f1.toDouble(), f1.toDouble(), f1.toDouble())
                val movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3)
                if (movingobjectposition1 != null) {
                    val d1 = vec31.distanceTo(movingobjectposition1.hitVec)
                    if (d1 < d0 || d0 == 0.0) {
                        entity = entity1
                        d0 = d1
                    }
                }
            }
        }
        if (entity != null) {
            movingobjectposition = MovingObjectPosition(entity)
        }
        if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.ENTITY) {
            setDead()
        }
    }

}