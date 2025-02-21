package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.manager.FactionManager;
import be.cepseudo.first_plugin.utils.CommandUtils;
import com.mojang.brigadier.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.UUID;
import static be.cepseudo.first_plugin.utils.CommandUtils.*;

public class ShowFactionCommand {
    private final FactionManager factionManager;

    public ShowFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("show")
                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                        .executes(this::execute))
                .executes(this::showUsage);
    }

    private int showUsage(CommandContext<CommandSourceStack> context) {
        return CommandUtils.showUsage(context, "/f show <faction>");
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        String factionName = context.getArgument("name", String.class);

        if (!factionManager.factionExists(factionName)) {
            sendMessage(player, "<red>❌ La faction <gold>'" + factionName + "'</gold> n'existe pas.");
            return Command.SINGLE_SUCCESS;
        }

        UUID leaderUUID = factionManager.getFactionLeader(factionName);
        if (leaderUUID == null) {
            sendMessage(player, "<yellow>⚠ Impossible de trouver le leader de la faction <gold>'" + factionName + "'</gold>.");
            return Command.SINGLE_SUCCESS;
        }

        String leaderName = Bukkit.getOfflinePlayer(leaderUUID).getName();
        sendMessage(player, "<green>🏰 La faction <gold>'" + factionName + "'</gold> est dirigée par <aqua>" + leaderName + "</aqua>.");

        return Command.SINGLE_SUCCESS;
    }
}
