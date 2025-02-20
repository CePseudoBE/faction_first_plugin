package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class CreateFactionCommand {
    private final FactionManager factionManager;

    public CreateFactionCommand(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("create")
                .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                        .executes(this::execute));
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Seuls les joueurs peuvent exécuter cette commande."));
            return Command.SINGLE_SUCCESS;
        }

        String factionName = context.getArgument("name", String.class);

        // Vérifie s'il y a un problème avec la création de faction
        String errorMessage = factionManager.canCreateFaction(factionName, player.getUniqueId());
        if (errorMessage != null) {
            sender.sendMessage(Component.text(errorMessage));
            return Command.SINGLE_SUCCESS;
        }

        // Création de la faction car tout est valide
        factionManager.createFaction(factionName, player.getUniqueId());
        sender.sendMessage(Component.text("Faction '" + factionName + "' créée avec succès."));
        return Command.SINGLE_SUCCESS;
    }

}
