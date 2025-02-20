package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;


public class DisbandFactionCommand {
    private final FactionManager factionManager;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public DisbandFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("disband")
                .executes(this::execute);
    }



    private int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(miniMessage.deserialize("<red>Seuls les joueurs peuvent exécuter cette commande."));
            return Command.SINGLE_SUCCESS;
        }

        if (!factionManager.isPlayerInFaction(player.getUniqueId())){
            sender.sendMessage(miniMessage.deserialize("<yellow>Vous n'appartenez à aucune faction."));
            return Command.SINGLE_SUCCESS;
        }

        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId())){
            sender.sendMessage(miniMessage.deserialize("<red>Vous n'avez pas le droit de dissoudre cette faction."));
            return Command.SINGLE_SUCCESS;
        }

        factionManager.deleteFaction(player.getUniqueId());

        sender.sendMessage(miniMessage.deserialize("<green>La faction <gold>'" + faction.getName() + "'</gold> a été dissoute."));
        return Command.SINGLE_SUCCESS;
    }
}
