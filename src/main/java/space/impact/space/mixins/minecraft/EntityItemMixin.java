package space.impact.space.mixins.minecraft;

import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import space.impact.space.utils.ext.Gravitation;

@Mixin(EntityItem.class)
public abstract class EntityItemMixin {
	
	@ModifyConstant(method = "onUpdate", constant = @Constant(doubleValue = 0.03999999910593033D), require = 1, remap = false)
	private double onOnUpdate(double value) {
		return Gravitation.setItemGravity((EntityItem) (Object) this);
	}
}
