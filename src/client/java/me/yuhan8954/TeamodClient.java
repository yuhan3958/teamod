package me.yuhan8954;

import net.fabricmc.api.ClientModInitializer;
import me.yuhan8954.block.ModBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import java.util.Random;

public class TeamodClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.TEA_PLANT, ChunkSectionLayer.CUTOUT);
	}
}