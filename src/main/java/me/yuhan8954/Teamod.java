package me.yuhan8954;

import me.yuhan8954.block.ModBlocks;
import me.yuhan8954.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Teamod implements ModInitializer {
	public static final String MOD_ID = "teamod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int SEMI_OXIDATION_TICKS = 20 * 20;   // 20초 예시
	public static final int FULL_OXIDATION_TICKS = 20 * 60;   // 60초 예시
	private static final String OX_KEY = "oxidation_ticks";

	@Override
	public void onInitialize() {
		LOGGER.info("茶!");
		LOGGER.info(String.valueOf(100/70));
		ModItems.initialize();
		ModBlocks.initialize();
		registerTeaOxidationHandler();
	}

	private void registerTeaOxidationHandler() {
		ServerTickEvents.END_WORLD_TICK.register((ServerLevel world) -> {
			for (ItemEntity itemEntity : world.getEntities(EntityType.ITEM, e -> !e.isRemoved())) {
				handleTeaOxidation(itemEntity);
			}
		});
	}

	private void handleTeaOxidation(ItemEntity entity) {
		ItemStack stack = entity.getItem();
		if (!isTeaLeaf(stack)) return;

		// 1) 현재 산화 틱 읽기 + 1 증가
		int ticks = getOxidationTicks(stack) + 1;
		setOxidationTicks(stack, ticks);

		// 2) 단계 전환 로직
		if (stack.is(ModItems.TEA_LEAVES)
				&& ticks >= SEMI_OXIDATION_TICKS && ticks < FULL_OXIDATION_TICKS) {

			// 비산화 → 반산화
			changeItem(entity, ModItems.SEMI_TEA_LEAVES, ticks);
			return;
		}

		if (stack.is(ModItems.SEMI_TEA_LEAVES)
				&& ticks >= FULL_OXIDATION_TICKS) {

			// 반산화 → 완전산화
			changeItem(entity, ModItems.OXIDIZED_TEA_LEAVES, ticks);
			return;
		}

		if (stack.is(ModItems.OXIDIZED_TEA_LEAVES)
				&& ticks > FULL_OXIDATION_TICKS) {

			// 완전 산화 이후에는 값 고정
			setOxidationTicks(stack, FULL_OXIDATION_TICKS);
		}
	}

	private boolean isTeaLeaf(ItemStack stack) {
		return stack.is(ModItems.TEA_LEAVES)
				|| stack.is(ModItems.SEMI_TEA_LEAVES)
				|| stack.is(ModItems.OXIDIZED_TEA_LEAVES);
	}

	// ─────────────────────────────
	//  NBT (산화 시간) 읽기/쓰기 헬퍼
	// ─────────────────────────────

	private int getOxidationTicks(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		if (data == null) {
			return 0;
		}
		CompoundTag tag = data.copyTag();
		return tag.getInt(OX_KEY).orElse(0); // 없으면 0 리턴
	}

	private void setOxidationTicks(ItemStack stack, int ticks) {
		// 기존 CUSTOM_DATA 유지하면서 해당 키만 업데이트
		CustomData.update(DataComponents.CUSTOM_DATA, stack, (CompoundTag tag) -> {
			tag.putInt(OX_KEY, ticks);
		});
	}

	private void changeItem(ItemEntity entity, Item newItem, int ticks) {
		ItemStack newStack = new ItemStack(newItem, entity.getItem().getCount());

		// 새 아이템에도 산화 시간 복사
		CompoundTag tag = new CompoundTag();
		tag.putInt(OX_KEY, ticks);
		CustomData.set(DataComponents.CUSTOM_DATA, newStack, tag);

		entity.setItem(newStack);
	}
}
