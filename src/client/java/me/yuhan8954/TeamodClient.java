package me.yuhan8954;

import net.fabricmc.api.ClientModInitializer;
import me.yuhan8954.block.ModBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class TeamodClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
          BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TEA_PLANT, RenderType.cutout());
	}
}
