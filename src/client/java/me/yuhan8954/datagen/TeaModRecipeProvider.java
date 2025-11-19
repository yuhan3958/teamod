package me.yuhan8954.datagen;

import java.util.concurrent.CompletableFuture;

import me.yuhan8954.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.world.item.Items;

public class TeaModRecipeProvider extends FabricRecipeProvider {
    public TeaModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
                shaped(RecipeCategory.MISC, ModItems.UNF_TEA_CUP, 1)
                        .pattern("# #")
                        .pattern("# #")
                        .pattern(" # ")
                        .define('#', Items.CLAY_BALL)
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "TeaModRecipeProvider";
    }


}