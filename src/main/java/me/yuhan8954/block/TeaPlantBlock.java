package me.yuhan8954.block;

import me.yuhan8954.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeaPlantBlock extends SweetBerryBushBlock {

    // 그대로 0..3 사용
    public static final IntegerProperty AGE = SweetBerryBushBlock.AGE;

    public TeaPlantBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0));
    }

    @Override
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl) {

    }

    // 자연 성장 처리 (원하는 속도로 수정 가능)
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE) < 3 && random.nextInt(10) == 0) {
            world.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 2);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState floorState, net.minecraft.world.level.BlockGetter world, BlockPos pos) {
        // farmland 위에서만 가능
        return floorState.is(Blocks.FARMLAND);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        int age = state.getValue(AGE);


        List<ItemStack> drops = super.getDrops(state, builder);

        if (age < 3) {
            drops.removeIf(stack ->
                    stack.is(ModItems.TEA_LEAVES) ||
                            stack.is(ModItems.SEMI_TEA_LEAVES) ||
                            stack.is(ModItems.OXIDIZED_TEA_LEAVES)
            );
            return drops;
        }

        RandomSource random = RandomSource.create();

        for (ItemStack stack : drops) {
            if (stack.is(ModItems.TEA_LEAVES)) {
                int extra = 0;
                while (random.nextFloat() < 0.2f) {
                    extra++;
                }
                stack.grow(extra); // 기존 3개 + extra
            }
        }

        return drops;
    }
    // 수확 상호작용
    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int age = state.getValue(AGE);
        if (age < 3) {
            return InteractionResult.PASS;
        }
        if (!(level instanceof ServerLevel server)) {
            return InteractionResult.SUCCESS;
        }

        // --- 1. LootParams.Builder 생성 ---
        LootParams.Builder lootBuilder = new LootParams.Builder(server)
                .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withOptionalParameter(LootContextParams.TOOL, player.getMainHandItem());

        List<ItemStack> drops = getDrops(state, lootBuilder);

        RandomSource random = server.getRandom();

        for (ItemStack stack : drops) {
            if (stack.is(ModItems.TEA_LEAVES)) {
                while (random.nextFloat() < 0.2f) {
                    stack.grow(1);
                }
            }
        }

        for (ItemStack drop : drops) {
            popResource(server, pos, drop);
        }

        server.setBlock(pos, state.setValue(AGE, 1), 2);

        server.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 1.0f);

        return InteractionResult.SUCCESS;
    }


    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        world.setBlock(pos, state.setValue(AGE, Math.min(3, age + 1)), 2);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.below());
        return below.is(Blocks.FARMLAND);
    }

}
