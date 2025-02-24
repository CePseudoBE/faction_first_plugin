package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.ClaimManager;
import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ClaimCommand extends BaseCommand {
    private final ClaimManager claimManager;
    private final FactionManager factionManager;

    public ClaimCommand(ClaimManager claimManager, FactionManager factionManager) {
        this.claimManager = claimManager;
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("claim")
                .executes(this::execute);
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        if (!factionManager.isPlayerInFaction(player.getUniqueId())) {
            sendMessage(player, "<yellow>⚠ Vous n'appartenez à aucune faction.");
            return Command.SINGLE_SUCCESS;
        }

        Chunk chunk = player.getLocation().getChunk();

        if(claimManager.isClaimed(chunk)) {
            sendMessage(player, "<red>❌ Ce chunk est déjà claim par une autre faction !");
            return Command.SINGLE_SUCCESS;
        }

        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());

        claimManager.claimChunk(chunk, faction);
        sendMessage(player, "<green>✅ Vous avez claim ce chunk");

        return Command.SINGLE_SUCCESS;
    }
}
