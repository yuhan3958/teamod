package me.yuhan8954.block;

import me.yuhan8954.Teamod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static void initialize() {

    }

    private static Block register(String name,
                                  Function<BlockBehaviour.Properties, Block> blockFactory,
                                  BlockBehaviour.Properties settings,
                                  boolean shouldRegisterItem) {

        // 블록 키
        ResourceKey<Block> blockKey =
                ResourceKey.create(Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(Teamod.MOD_ID, name));

        // 블록 인스턴스 생성
        Block block = blockFactory.apply(settings.setId(blockKey));

        // BlockItem도 같이 등록하고 싶으면
        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey =
                    ResourceKey.create(Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(Teamod.MOD_ID, name));

            BlockItem blockItem = new BlockItem(
                    block,
                    new Item.Properties()
                            .setId(itemKey)
                            .useBlockDescriptionPrefix() // 이름을 block.xxx 로 공유
            );

            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        // 블록 등록
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static final Block TEA_PLANT = register(
            "tea_plant",
            TeaPlantBlock::new,
            BlockBehaviour.Properties
                    .of()                   // 기본 식물 재질
                    .noCollision()         // 충돌 없음 (플레이어가 통과)
                    .instabreak()           // 바로 부서짐
                    .randomTicks()          // randomTick() 호출
                    .sound(SoundType.GRASS),
            true
    );
}
