package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.manager.FactionManager;
import be.cepseudo.first_plugin.manager.PlayerManager;
import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.utils.CommandUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public class InvitInFactionCommand extends BaseCommand {
    private final FactionManager factionManager;
    private final PlayerManager playerManager;

    public InvitInFactionCommand(FactionManager factionManager, PlayerManager playerManager) {
        this.factionManager = factionManager;
        this.playerManager = playerManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("invite")
                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("player", StringArgumentType.word())
                        .executes(this::execute))
                .executes(this::showUsage);
    }

    private int showUsage(CommandContext<CommandSourceStack> context) {
        return CommandUtils.showUsage(context, "/f invite <player>");
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        String targetPlayerName = context.getArgument("player", String.class);

        // Vérifier si le joueur est dans une faction
        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            sendMessage(player, "<yellow>⚠ Vous devez être dans une faction pour inviter quelqu'un.");
            return Command.SINGLE_SUCCESS;
        }

        // Vérifier si le joueur est le leader de la faction
        if (!faction.getLeader().equals(player.getUniqueId())) {
            sendMessage(player, "<red>⛔ Seul le leader de la faction peut inviter des membres.");
            return Command.SINGLE_SUCCESS;
        }

        // Récupérer l'UUID du joueur cible
        UUID targetUUID = factionManager.getUUIDFromPlayerName(targetPlayerName);
        if (targetUUID == null) {
            sendMessage(player, "<red>❌ Ce joueur n'a jamais rejoint le serveur.");
            return Command.SINGLE_SUCCESS;
        }

        // Vérifier si le joueur cible a déjà une faction
        if (factionManager.isPlayerInFaction(targetUUID)) {
            sendMessage(player, "<yellow>⚠ Ce joueur fait déjà partie d'une faction.");
            return Command.SINGLE_SUCCESS;
        }

        // Ajouter l'invitation
        factionManager.inviteToFaction(player.getUniqueId(), faction, targetPlayerName, playerManager);

        // Envoyer un message au joueur invité
        Player targetPlayer = Bukkit.getPlayer(targetUUID);
        if (targetPlayer != null) {
            sendMessage(targetPlayer, "<green>📩 Vous avez été invité à rejoindre la faction <gold>" + faction.getName() + "</gold> par <aqua>" + player.getName() + "</aqua>.");
            sendMessage(targetPlayer, "<yellow>💡 Tapez /f join " + faction.getName() + " pour accepter.");
        }

        sendMessage(player, "<green>✅ Vous avez invité <aqua>" + targetPlayerName + "</aqua> dans votre faction.");
        return Command.SINGLE_SUCCESS;
    }
}
