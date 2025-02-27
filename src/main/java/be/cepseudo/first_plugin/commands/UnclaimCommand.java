package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.enums.FactionRole;
import be.cepseudo.first_plugin.manager.ClaimManager;
import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class UnclaimCommand extends BaseCommand {
    private final ClaimManager claimManager;
    private final FactionManager factionManager;

    public UnclaimCommand(ClaimManager claimManager, FactionManager factionManager) {
        this.claimManager = claimManager;
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("unclaim")
                .executes(this::execute);
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        // Vérifier si le joueur est dans une faction
        Faction factionPlayer = factionManager.getFactionByPlayer(player.getUniqueId());
        if (factionPlayer == null) {
            sendMessage(player, "<yellow>⚠ Vous devez être dans une faction pour unclaim un chunk.");
            return Command.SINGLE_SUCCESS;
        }

        // Vérification du rôle (Leader ou Officer peuvent unclaim)
        FactionRole role = factionPlayer.getMembers().get(player.getUniqueId());
        if (role == null || !role.canClaim()) {
            sendMessage(player, "<red>⛔ Seul un leader ou un officier peut unclaim des chunks.");
            return Command.SINGLE_SUCCESS;
        }

        Chunk chunk = player.getLocation().getChunk();

        // Vérifier si le chunk est claim
        Faction factionClaim = claimManager.getFactionByChunk(chunk);
        if (factionClaim == null) {
            sendMessage(player, "<red>❌ Ce chunk n'est pas claim. Utilisez /f claim pour le capturer.");
            return Command.SINGLE_SUCCESS;
        }

        // Vérifier si la faction du joueur possède bien le chunk
        if (!factionClaim.equals(factionPlayer)) {
            sendMessage(player, "<red>❌ Ce chunk appartient à la faction <gold>" + factionClaim.getName() + "</gold>, vous ne pouvez pas l'unclaim !");
            return Command.SINGLE_SUCCESS;
        }

        // Unclaim du chunk
        claimManager.unclaimChunk(chunk);
        sendMessage(player, "<green>✅ Vous avez unclaim ce chunk pour votre faction <gold>" + factionPlayer.getName() + "</gold>.");

        return Command.SINGLE_SUCCESS;
    }
}
