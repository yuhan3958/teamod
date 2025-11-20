package me.yuhan8954.datagen;

import java.util.concurrent.CompletableFuture;

import me.yuhan8954.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class TeaModRecipeProvider extends FabricRecipeProvider {

    public TeaModRecipeProvider(FabricDataOutput output,
                                CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    // ★ createRecipeProvider 지우고, 이 메서드만 구현
    @Override
    protected void buildRecipes(RecipeOutput output) {
        // shaped(...) / has(...) 는 RecipeProvider 가 제공하는 헬퍼들
        shaped(RecipeCategory.MISC, ModItems.UNF_TEA_CUP, 1)
                .pattern("# #")
                .pattern("# #")
                .pattern(" # ")
                .define('#', Items.CLAY_BALL)
                // ★ 이거 안 넣으면 1.21대에서 유효성 검사에서 막히는 경우가 있음
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(output);
    }

    @Override
    public String getName() {
        return "TeaModRecipeProvider";
    }
}
