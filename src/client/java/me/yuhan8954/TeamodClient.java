package me.yuhan8954;

import me.yuhan8954.Teamod;
import me.yuhan8954.block.ModBlocks;
import me.yuhan8954.item.ModItems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;


import java.util.List;

public class TeamodClient implements ClientModInitializer {

	private static final String OX_KEY = "oxidation_ticks";

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.TEA_PLANT, ChunkSectionLayer.CUTOUT);
		ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag, List<Component> tooltip) -> {

			if (isTeaLeaf(stack)) {
				int ticks = getOxidationTicks(stack);

				int percent = (int)((ticks / (double) Teamod.FULL_OXIDATION_TICKS) * 100);
				if (percent > 100) percent = 100;

				String stage;
				if (percent < 20) stage = "비산화";
				else if (percent < 70) stage = "중간 산화";
				else stage = "완전 산화";

				tooltip.add(Component.literal("§a산화도: §f" + percent + "% (" + stage + ")"));
			}
		});
	}

	private boolean isTeaLeaf(ItemStack stack) {
		return stack.is(ModItems.TEA_LEAVES)
				|| stack.is(ModItems.SEMI_TEA_LEAVES)
				|| stack.is(ModItems.OXIDIZED_TEA_LEAVES);
	}

	private int getOxidationTicks(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		if (data == null) return 0;

		var tag = data.copyTag(); // Optional 기반 구조
		return tag.getInt(OX_KEY).orElse(0);
	}
}
