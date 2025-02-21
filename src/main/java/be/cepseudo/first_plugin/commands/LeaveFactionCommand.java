package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import static be.cepseudo.first_plugin.utils.CommandUtils.*;

public class LeaveFactionCommand {
    private final FactionManager factionManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public LeaveFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("leave")
                .executes(this::execute);
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        if (!factionManager.isPlayerInFaction(player.getUniqueId())) {
            sendMessage(player, "<yellow>⚠ Vous n'appartenez à aucune faction.");
            return Command.SINGLE_SUCCESS;
        }

        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());

        if (faction.getLeader().equals(player.getUniqueId())) {
            sendMessage(player, "<red>⛔ Vous êtes le leader de cette faction. Utilisez /f disband ou promouvez quelqu'un d'autre.");
            return Command.SINGLE_SUCCESS;
        }

        factionManager.leaveFaction(player.getUniqueId());
        factionManager.broadcastToFaction(faction, miniMessage.deserialize("<yellow>⚠ <aqua>" + player.getName() + "</aqua> a quitté votre faction."));
        sendMessage(player, "<green>✅ Vous avez quitté votre faction.");

        return Command.SINGLE_SUCCESS;
    }
}
