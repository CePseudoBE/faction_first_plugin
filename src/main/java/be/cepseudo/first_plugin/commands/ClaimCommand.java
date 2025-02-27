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

        // Vérification que le joueur est dans une faction
        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            sendMessage(player, "<yellow>⚠ Vous devez être dans une faction pour claim un chunk.");
            return Command.SINGLE_SUCCESS;
        }

        // Vérification du rôle (Leader ou Officer peuvent claim)
        FactionRole role = faction.getMembers().get(player.getUniqueId());
        if (role == null || !role.canClaim()) {
            sendMessage(player, "<red>⛔ Seul un leader ou un officier peut claim des chunks.");
            return Command.SINGLE_SUCCESS;
        }

        Chunk chunk = player.getLocation().getChunk();

        // Vérifier si le chunk est déjà claimé
        if (claimManager.isClaimed(chunk)) {
            sendMessage(player, "<red>❌ Ce chunk appartient déjà à une faction !");
            return Command.SINGLE_SUCCESS;
        }

        // Claim du chunk
        claimManager.claimChunk(chunk, faction);
        sendMessage(player, "<green>✅ Vous avez claim ce chunk pour votre faction <gold>" + faction.getName() + "</gold>.");

        return Command.SINGLE_SUCCESS;
    }
}

