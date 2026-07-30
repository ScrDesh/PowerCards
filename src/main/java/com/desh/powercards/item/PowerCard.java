package com.desh.powercards.item;

import com.desh.powercards.deckclasses.DeckAttachment;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class PowerCard extends Item {
    public PowerCard(Properties properties) {super(properties);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack stack = player.getItemInHand(usedHand);

        LogUtils.getLogger().debug("" + player.getData(DeckAttachment.DECK_DATA).getBaseBP());

        if (level.isClientSide()) {return InteractionResultHolder.consume(stack);}

        else if (player.getData(DeckAttachment.DECK_DATA).getBaseBP() >= 27) {
            LogUtils.getLogger().debug("Failed at " + player.getData(DeckAttachment.DECK_DATA).getBaseBP());
            player.displayClientMessage(Component.translatable("ui.powercards.max_bp_reached").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.sidedSuccess(stack, false);
        }
        else {
            LogUtils.getLogger().debug("Succeeded at " + player.getData(DeckAttachment.DECK_DATA).getBaseBP());
            player.level().playSound(
                    player,
                    player.getBlockPosBelowThatAffectsMyMovement(),
                    SoundEvents.SNOW_GOLEM_SHEAR,
                    SoundSource.PLAYERS,
                    0.4f,
                    1f
            );

            player.displayClientMessage(Component.translatable("ui.powercards.plus1bp").withStyle(ChatFormatting.GREEN), true);

            player.getData(DeckAttachment.DECK_DATA).addBP();

            stack.shrink(1);

            return InteractionResultHolder.sidedSuccess(stack, false);

        }


    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> lines, TooltipFlag flag) {

        lines.add(Component.translatable("description.powercards.onrightclick").withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("description.powercards.useforbp").withStyle(ChatFormatting.BLUE));
    }
}