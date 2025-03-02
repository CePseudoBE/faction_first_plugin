package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.manager.FactionManager;
import be.cepseudo.first_plugin.utils.CommandUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public class CreateFactionCommand extends BaseCommand {
    private final FactionManager factionManager;

    public CreateFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("create")
                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder
                        .<CommandSourceStack, String>argument("name", StringArgumentType.word())
                        .executes(this::execute))
                .executes(this::showUsage);
    }

    private int showUsage(CommandContext<CommandSourceStack> context) {
        return CommandUtils.showUsage(context, "/f create <name>");
    }

    /**
     * Exécute la création de faction.
     */
    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        // Récupération du nom de faction
        String factionName = context.getArgument("name", String.class);

        // Vérifie la longueur
        if (factionName.length() > 15) {
            sendMessage(player, "<red>⛔ Le nom de faction ne peut pas dépasser 15 caractères.");
            return Command.SINGLE_SUCCESS;
        }

        // Vérifie si la création est possible
        String errorMessage = factionManager.canCreateFaction(factionName, player.getUniqueId());
        if (errorMessage != null) {
            sendMessage(player, "<yellow>⚠ " + errorMessage);
            return Command.SINGLE_SUCCESS;
        }

        // Création de la faction
        factionManager.createFaction(factionName, player.getUniqueId());
        sendMessage(player, "<green>✅ Faction <gold>'" + factionName + "'</gold> créée avec succès! 🎉");

        return Command.SINGLE_SUCCESS;
    }
}
