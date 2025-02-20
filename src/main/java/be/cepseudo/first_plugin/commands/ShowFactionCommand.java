package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.UUID;

public class ShowFactionCommand {
    private final FactionManager factionManager;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage(); // MiniMessage pour un texte coloré

    public ShowFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("show")
                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                        .executes(this::execute));
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();

        String factionName = context.getArgument("name", String.class);

        if (!factionManager.factionExists(factionName)) {
            sender.sendMessage(miniMessage.deserialize("<red>❌ La faction <gold>'" + factionName + "'</gold> n'existe pas."));
            return Command.SINGLE_SUCCESS;
        }

        UUID leaderUUID = factionManager.getFactionLeader(factionName);
        if (leaderUUID == null) {
            sender.sendMessage(miniMessage.deserialize("<yellow>⚠ Impossible de trouver le leader de la faction <gold>'" + factionName + "'</gold>."));
            return Command.SINGLE_SUCCESS;
        }

        String leaderName = Bukkit.getOfflinePlayer(leaderUUID).getName();
        sender.sendMessage(miniMessage.deserialize("<green>🏰 La faction <gold>'" + factionName + "'</gold> est dirigée par <aqua>" + leaderName + "</aqua>."));

        return Command.SINGLE_SUCCESS;
    }
}
