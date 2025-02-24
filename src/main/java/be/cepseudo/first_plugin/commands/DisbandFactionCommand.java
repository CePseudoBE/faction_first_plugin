package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public class DisbandFactionCommand extends BaseCommand {
    private final FactionManager factionManager;

    public DisbandFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("disband")
                .executes(this::execute);
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        if (!factionManager.isPlayerInFaction(player.getUniqueId())) {
            sendMessage(player, "<yellow>Vous n'appartenez à aucune faction.");
            return Command.SINGLE_SUCCESS;
        }

        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());

        if (!faction.getLeader().equals(player.getUniqueId())) {
            sendMessage(player, "<red>Vous n'avez pas le droit de dissoudre cette faction.");
            return Command.SINGLE_SUCCESS;
        }

        factionManager.deleteFaction(player.getUniqueId());
        sendMessage(player, "<green>La faction <gold>'" + faction.getName() + "'</gold> a été dissoute.");
        return Command.SINGLE_SUCCESS;
    }
}

