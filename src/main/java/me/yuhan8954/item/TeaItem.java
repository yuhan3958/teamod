package me.yuhan8954.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TeaItem extends Item {
    private final TeaType type;
    private final Item cupItem;
    private final Item potItem;

    public TeaItem(Properties props, TeaType type, Item cupItem, Item potItem) {
        super(props);
        this.type = type;
        this.cupItem = cupItem;
        this.potItem = potItem;
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }


    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 효과 적용은 서버에서만
        if (!level.isClientSide()) {
            for (MobEffectInstance effect : type.effects()) {
                // 매번 새 인스턴스를 주는 게 안전 (참조 공유 방지)
                entity.addEffect(new MobEffectInstance(effect));
            }
        }

        // 플레이어가 아니면 그냥 소모만
        if (!(entity instanceof Player player)) {
            stack.shrink(1);
            return stack;
        }

        // 크리에이티브면 소모/반환 없음
        if (player.getAbilities().instabuild) {
            return stack;
        }

        // 1개 소모
        stack.shrink(1);

        // 컵 반환
        ItemStack cup = new ItemStack(cupItem);
        ItemStack pot = new ItemStack(potItem);

        // 스택이 비었으면 손에 들고 있던 자리에 컵
        if (stack.isEmpty()) {
            return cup;
        }

        // 아니면 인벤에 넣고, 실패하면 드랍
        if (!player.getInventory().add(cup)) {
            player.drop(cup, false);
        }

        // pot 처리
        if (!player.getInventory().add(pot)) {
            player.drop(pot, false);
        }

        return stack;
    }
}