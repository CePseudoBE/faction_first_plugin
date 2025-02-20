package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandFactionCommand {
    private final FactionManager factionManager;

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
            sender.sendMessage(Component.text("Seuls les joueurs peuvent exécuter cette commande."));
            return Command.SINGLE_SUCCESS;
        }

        if (!factionManager.isPlayerInFaction(player.getUniqueId())){
            sender.sendMessage(Component.text("Vous n'appartenez à aucune faction."));
            return Command.SINGLE_SUCCESS;
        }

        Faction faction = factionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId())){
            sender.sendMessage(Component.text("Vous n'avez pas le droit de dissoudre cette faction."));
            return Command.SINGLE_SUCCESS;
        }

        factionManager.deleteFaction(player.getUniqueId());

        sender.sendMessage(Component.text("La faction '" + faction.getName() + "' a été dissoute."));
        return Command.SINGLE_SUCCESS;
    }
}
