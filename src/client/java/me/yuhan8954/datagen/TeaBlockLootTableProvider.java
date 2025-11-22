package me.yuhan8954.datagen;

import me.yuhan8954.block.ModBlocks;
import me.yuhan8954.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class TeaBlockLootTableProvider extends FabricBlockLootTableProvider {

    public TeaBlockLootTableProvider(FabricDataOutput output,
                                     CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generate() {
        this.add(ModBlocks.TEA_PLANT, block ->
                LootTable.lootTable()
                        // 씨앗 100% 1개
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.TEA_SEED)))
                        // 잎 기본 3개
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.TEA_LEAVES)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))))
        );
    }
}