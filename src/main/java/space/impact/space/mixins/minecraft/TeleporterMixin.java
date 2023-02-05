package space.impact.space.mixins.minecraft;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Teleporter.class)
public class TeleporterMixin {
	
	@Inject(method = "makePortal", at = @At("HEAD"), remap = false, cancellable = true)
	public void makePortal(Entity e, CallbackInfoReturnable<Boolean> cir) {
		System.out.println("NETHER PORTAL DISABLED");
		cir.setReturnValue(false);
	}
}
