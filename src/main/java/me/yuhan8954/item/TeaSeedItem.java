package me.yuhan8954.item;

import me.yuhan8954.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class TeaSeedItem extends Item {

    public TeaSeedItem(Properties props) {
        super(props);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos().above();
        Player player = ctx.getPlayer();

        // 아래 블록이 farmland인지 확인
        if (!level.getBlockState(ctx.getClickedPos()).is(Blocks.FARMLAND)) {
            return InteractionResult.FAIL;
        }

        // 빈 공간인지 확인
        if (!level.getBlockState(pos).isAir()) {
            return InteractionResult.FAIL;
        }

        // 서버에서만 설치
        if (!level.isClientSide()) {
            level.setBlock(pos, ModBlocks.TEA_PLANT.defaultBlockState(), 3);

            if (player != null && !player.getAbilities().instabuild) {
                ctx.getItemInHand().shrink(1); // 씨앗 1개 소모
            }
        }

        return InteractionResult.SUCCESS;
    }
}
