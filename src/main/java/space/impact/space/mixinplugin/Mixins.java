package space.impact.space.mixinplugin;

import cpw.mods.fml.relauncher.FMLLaunchHandler;

import java.util.Arrays;
import java.util.List;

public enum Mixins {
	
	EntityItemMixin("minecraft.EntityItemMixin", TargetedMod.VANILLA),
	EntityLivingBaseMixin("minecraft.EntityLivingBaseMixin", TargetedMod.VANILLA),
	EntityRendererMixin("minecraft.EntityRendererMixin", Side.CLIENT, TargetedMod.VANILLA),
	TeleporterMixin("minecraft.TeleporterMixin", TargetedMod.VANILLA),
	;
	
	public final String mixinClass;
	public final List<TargetedMod> targetedMods;
	private final Side side;
	
	Mixins(String mixinClass, Side side, TargetedMod... targetedMods) {
		this.mixinClass = mixinClass;
		this.targetedMods = Arrays.asList(targetedMods);
		this.side = side;
	}
	
	Mixins(String mixinClass, TargetedMod... targetedMods) {
		this.mixinClass = mixinClass;
		this.targetedMods = Arrays.asList(targetedMods);
		this.side = Side.BOTH;
	}
	
	public boolean shouldLoad(List<TargetedMod> loadedMods) {
		return (side == Side.BOTH
				|| side == Side.SERVER && FMLLaunchHandler.side().isServer()
				|| side == Side.CLIENT && FMLLaunchHandler.side().isClient())
				&& loadedMods.containsAll(targetedMods);
	}
	
	enum Side {
		BOTH,
		CLIENT,
		SERVER;
	}
}


