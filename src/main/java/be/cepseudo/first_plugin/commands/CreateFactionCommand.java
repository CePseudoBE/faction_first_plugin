package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import com.mojang.brigadier.arguments.StringArgumentType;
import static be.cepseudo.first_plugin.utils.CommandUtils.*;

public class CreateFactionCommand {
    private final FactionManager factionManager;

    public CreateFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("create")
                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                        .executes(this::execute));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        String factionName = context.getArgument("name", String.class);

        // Vérifie s'il y a un problème avec la création de faction
        String errorMessage = factionManager.canCreateFaction(factionName, player.getUniqueId());
        if (errorMessage != null) {
            sendMessage(player, "<yellow>⚠ " + errorMessage);
            return Command.SINGLE_SUCCESS;
        }

        // Création de la faction car tout est valide
        factionManager.createFaction(factionName, player.getUniqueId());
        sendMessage(player, "<green>✅ Faction <gold>'" + factionName + "'</gold> créée avec succès! 🎉");

        return Command.SINGLE_SUCCESS;
    }
}
