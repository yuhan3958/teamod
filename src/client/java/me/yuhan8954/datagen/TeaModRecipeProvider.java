package me.yuhan8954.datagen;

import me.yuhan8954.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TeaModRecipeProvider extends FabricRecipeProvider {
    public TeaModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, ModItems.UNF_TEA_CUP, 1)
                        .pattern("# #")
                        .pattern("# #")
                        .pattern(" # ")
                        .unlockedBy("has_clay", has(Items.CLAY_BALL))
                        .define('#', Items.CLAY_BALL)
                        .save(output);

                shaped(RecipeCategory.MISC, ModItems.UNF_TEA_POT, 1)
                        .pattern(" # ")
                        .pattern("# #")
                        .pattern("###")
                        .unlockedBy("has_clay", has(Items.CLAY_BALL))
                        .define('#', Items.CLAY_BALL)
                        .save(output);

                oreSmelting(List.of(ModItems.UNF_TEA_CUP), RecipeCategory.MISC, ModItems.TEA_CUP, 0.3f, 200, "teacup");
                oreSmelting(List.of(ModItems.UNF_TEA_POT), RecipeCategory.MISC, ModItems.TEA_POT, 0.3f, 200, "teapot");
                oreSmelting(List.of(ModItems.WATER_TEA_POT), RecipeCategory.MISC, ModItems.HOT_WATER_TEA_POT, 2f, 300, "water_teapot");
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return "TeaModRecipeProvider";
    }


}