package me.yuhan8954.LootTable;

import me.yuhan8954.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Optional;

public final class grassLootTable {
    private static boolean isBlockLoot(ResourceKey<LootTable> key) {
        Optional<ResourceKey<LootTable>> opt = Blocks.SHORT_GRASS.getLootTable();
        return opt.isPresent() && opt.get().equals(key);
    }

    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (!source.isBuiltin()) return;

            if (!isBlockLoot(key)) return;

            LootPool.Builder pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.02f))
                    .add(LootItem.lootTableItem(ModItems.TEA_SEED));

            tableBuilder.withPool(pool);
        });
    }
}