package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.FactionManager;
import be.cepseudo.first_plugin.manager.PlayerManager;
import be.cepseudo.first_plugin.utils.CommandUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public class JoinFactionCommand extends BaseCommand {
    private final FactionManager factionManager;
    private final PlayerManager playerManager;

    public JoinFactionCommand(FactionManager factionManager, PlayerManager playerManager) {
        this.factionManager = factionManager;
        this.playerManager = playerManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("join")
                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                        .executes(this::execute))
                .executes(this::showUsage);
    }

    private int showUsage(CommandContext<CommandSourceStack> context) {
        return CommandUtils.showUsage(context, "/f join <faction>");
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayerOrSendError(context);
        if (player == null) return Command.SINGLE_SUCCESS;

        String targetFactionName = context.getArgument("faction", String.class);

        if(factionManager.isPlayerInFaction(player.getUniqueId())){
            sendMessage(player, "<red>Vous avez déjà une faction, quittez la pour en rejoindre une autre.");
            return Command.SINGLE_SUCCESS;
        }

        if(!factionManager.factionExists(targetFactionName)){
            sendMessage(player, "<red>Cette faction n'existe pas.");
            return Command.SINGLE_SUCCESS;
        }

        if(!factionManager.isInvited(player.getUniqueId())){
            sendMessage(player, "<red>Vous n'avez pas été invité dans cette faction.");
            return Command.SINGLE_SUCCESS;
        }

        factionManager.newPlayerInFaction(player.getUniqueId());
        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());
        sendMessage(player, "<green>Félicitations, vous venez de rejoindre la faction : " + targetFactionName + ".");
        factionManager.broadcastToFaction(faction, "<green>📢 <aqua>" + player.getName() + "</aqua> a rejoint votre faction !");
        return Command.SINGLE_SUCCESS;
    }
}
