package space.impact.space.mixins.minecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import space.impact.space.utils.ext.Gravitation;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
	
	public EntityLivingBaseMixin() {
		super(null);
	}
	
	@ModifyConstant(method = "moveEntityWithHeading(FF)V", constant = @Constant(doubleValue = 0.08D), require = 1, remap = false)
	private double onMoveEntityWithHeading(double value) {
		return Gravitation.setGravityEntity(this);
	}
	
}
