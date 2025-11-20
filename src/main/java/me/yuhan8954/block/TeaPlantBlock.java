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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

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

    // 수확 상호작용
    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos,
                                                        Player player, BlockHitResult hit) {

        int age = state.getValue(AGE);

        // AGE 3에서만 수확 가능
        if (age == 3) {
            if (!world.isClientSide()) {

                // TeaMod 전용 드롭
                ItemStack drop = new ItemStack(ModItems.TEA_LEAVES);

                popResource(world, pos, drop);

                // 나무 상태를 AGE 1로 되돌림
                BlockState newState = state.setValue(AGE, 1);
                world.setBlock(pos, newState, 2);

                // 소리
                world.playSound(null, pos,
                        SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                        SoundSource.BLOCKS,
                        1.0F,
                        0.8F + world.getRandom().nextFloat() * 0.4F);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    // Bone meal 로직 (베리 덤불은 이미 구현해서 override만 조정)
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
