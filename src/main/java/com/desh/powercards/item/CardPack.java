package com.desh.powercards.item;

import com.desh.powercards.CardDefinition;
import com.desh.powercards.CardItem;
import com.desh.powercards.ModCards;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardPack extends Item {
    public CardPack(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        player.level().playSound(
                null,
                player.getBlockPosBelowThatAffectsMyMovement(),
                SoundEvents.WOLF_ARMOR_BREAK,
                SoundSource.PLAYERS,
                1.0f,
                1f
        );

        int opencount = player.isCrouching() ? (stack.getMaxDamage()-stack.getDamageValue()) : 1;

        for (int i = 0; i < opencount; i++) {
            double chance = Math.random();
            ArrayList<CardItem> pool;

            //26, 22, 16, 8, 4

            if (chance < 0.26) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.COMMON);}
            else if (chance < 0.48) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.UNCOMMON);}
            else if (chance < 0.64) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.RARE);}
            else if (chance < 0.72) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.EPIC);}
            else {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.LEGENDARY);}

            int choice = (int)(Math.random() * pool.size());
            ItemStack item = new ItemStack(pool.get(choice).asItem());
            if (chance > 0.76) {item = new ItemStack(ModCards.POWER_CARD.get());}
            ItemEntity ientity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), item); ientity.spawnAtLocation(item);
        }

        stack.hurtAndBreak(opencount, player, EquipmentSlot.MAINHAND);

        return InteractionResultHolder.consume(stack);
    }

    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable("ui.powercards.cardpacktext").withStyle(ChatFormatting.GRAY));
    }
}
