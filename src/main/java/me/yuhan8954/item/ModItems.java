package me.yuhan8954.item;

import me.yuhan8954.Teamod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(ModItems.TEA_LEAVES));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(ModItems.SEMI_TEA_LEAVES));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(ModItems.OXIDIZED_TEA_LEAVES));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(ModItems.UNF_TEA_CUP));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((itemGroup) -> itemGroup.accept(ModItems.UNF_TEA_CUP));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(ModItems.TEA_CUP));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((itemGroup) -> itemGroup.accept(ModItems.TEA_CUP));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(ModItems.UNF_TEA_POT));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((itemGroup) -> itemGroup.accept(ModItems.UNF_TEA_POT));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((itemGroup) -> itemGroup.accept(ModItems.TEA_POT));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((itemGroup) -> itemGroup.accept(ModItems.TEA_POT));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                        .register(itemGroup -> itemGroup.accept(ModItems.TEA_SEED));

        CompostingChanceRegistry.INSTANCE.add(ModItems.TEA_LEAVES, 1f);
        CompostingChanceRegistry.INSTANCE.add(ModItems.SEMI_TEA_LEAVES, 1f);
        CompostingChanceRegistry.INSTANCE.add(ModItems.OXIDIZED_TEA_LEAVES, 1f);
    }
    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Teamod.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static final Item TEA_LEAVES = register("tea_leaves", //fresh
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(1f).alwaysEdible().build()));
    public static final Item SEMI_TEA_LEAVES =
            register("semi_tea_leaves", Item::new, new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).alwaysEdible().build()));
    public static final Item OXIDIZED_TEA_LEAVES =
            register("oxidized_tea_leaves", Item::new, new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(0).saturationModifier(0f).alwaysEdible().build()));

    public static final Item UNF_TEA_CUP = register("unfired_tea_cup", Item::new, new Item.Properties());
    public static final Item TEA_CUP = register("tea_cup", Item::new, new Item.Properties());
    public static final Item UNF_TEA_POT = register("unfired_tea_pot", Item::new, new Item.Properties());
    public static final Item TEA_POT = register("tea_pot", Item::new, new Item.Properties());

    public static final Item TEA_SEED = register(
            "tea_seed",
            TeaSeedItem::new,
            new Item.Properties()
    );
}