package space.impact.space.mixins.minecraft;

import ic2.core.IC2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.impact.space.api.world.space.IZeroGravitationProvider;
import space.impact.space.utils.ext.Gravitation;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
	
	private float timeMotionForward = 0f;
	private float timeMotionJump = 0f;
	private float timeMotionSneak = 0f;
	
	public EntityLivingBaseMixin() {
		super(null);
	}
	
	@ModifyConstant(method = "moveEntityWithHeading(FF)V", constant = @Constant(doubleValue = 0.08D), require = 1)
	private double onMoveEntityWithHeading(double value) {
		return Gravitation.setGravityEntity(this);
	}
	
	@Inject(method = "onEntityUpdate", at = @At("TAIL"))
	public void onEntityUpdate$mixin(CallbackInfo ci) {
		if (worldObj.provider instanceof IZeroGravitationProvider) {
			Entity e = this;
			if (e instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) e;
				if (IC2.keyboard.isForwardKeyDown(player)) {
					player.moveFlying(0.0F, 0.92F, 0.02F);
				}

				double prevmotion = player.motionY;
				player.motionY = Math.min(player.motionY + (double) (0.2F), 0.6000000238418579);

				float maxHoverY = 0.0F;
				if (IC2.keyboard.isJumpKeyDown(player)) {
					maxHoverY = 0.1F;
				}

				if (IC2.keyboard.isSneakKeyDown(player)) {
					maxHoverY = -0.1F;
				}

				if (player.motionY > (double) maxHoverY) {
					player.motionY = maxHoverY;
					if (prevmotion > player.motionY) {
						player.motionY = prevmotion;
					}
				}
				
				player.fallDistance = 0.0F;
				player.distanceWalkedModified = 0.0F;
				IC2.platform.resetPlayerInAirTime(player);
			}
		}
	}
}
