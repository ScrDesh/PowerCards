package com.desh.powercards;

import com.desh.powercards.deckclasses.DeckAttachment;
import com.desh.powercards.deckclasses.DeckMenu;
import com.desh.powercards.deckclasses.PlayerDeckData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deckdebug")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("status")
                                .executes(ModCommands::executeStatus))
                        .then(Commands.literal("bp")
                                .then(Commands.literal("add")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ModCommands::executeAddBP)))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(ModCommands::executeSetBP)))
                                .then(Commands.literal("reset")
                                        .executes(ModCommands::executeResetBP)))
                        .then(Commands.literal("open")
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    player.openMenu(new SimpleMenuProvider(
                                            (id, inv, p) -> new DeckMenu(id, inv, true),
                                            Component.literal("Deck")
                                    ));
                                    return Command.SINGLE_SUCCESS;
                                }))
        );
    }

    private static int executeStatus(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        PlayerDeckData data = player.getData(DeckAttachment.DECK_DATA);

        ctx.getSource().sendSuccess(() -> Component.literal(
                "=== Deck Status ===\n" +
                        "Total BP: "   + data.getTotalBP() + "\n" +
                        "Deck Cost: "  + data.getDeckInventory().getTotalEquippedCost() + "\n" +
                        "Deck Valid: " + data.isDeckValid()
        ), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int executeAddBP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        PlayerDeckData data = player.getData(DeckAttachment.DECK_DATA);

        data.awardBP(amount);

        ctx.getSource().sendSuccess(() -> Component.literal(
                "Added " + amount + " BP. Total: " + data.getTotalBP()
        ), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int executeSetBP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        PlayerDeckData data = player.getData(DeckAttachment.DECK_DATA);

        data.setBP(amount);

        ctx.getSource().sendSuccess(() -> Component.literal(
                "Set BP to " + data.getTotalBP()
        ), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int executeResetBP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        PlayerDeckData data = player.getData(DeckAttachment.DECK_DATA);

        data.setBP(0);

        ctx.getSource().sendSuccess(() -> Component.literal("BP reset to 0."), false);

        return Command.SINGLE_SUCCESS;
    }
}