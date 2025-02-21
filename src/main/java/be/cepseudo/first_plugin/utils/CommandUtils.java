package be.cepseudo.first_plugin.utils;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.mojang.brigadier.context.CommandContext;

public class CommandUtils {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Récupère le joueur depuis le contexte de commande.
     *
     * @param context Contexte de la commande.
     * @return Le joueur, ou null si ce n'est pas un joueur.
     */
    public static Player getPlayerOrSendError(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(miniMessage.deserialize("<red>Seuls les joueurs peuvent exécuter cette commande."));
            return null;
        }
        return player;
    }

    /**
     * Envoie un message au joueur.
     *
     * @param sender  Le joueur.
     * @param message Le message à envoyer.
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(miniMessage.deserialize(message));
    }

    public static int showUsage(CommandContext<CommandSourceStack> context, String usage) {
        Player player = getPlayerOrSendError(context);
        if (player != null) {
            sendMessage(player, "<red>❌ Utilisation correcte : " + usage);
        }

        return Command.SINGLE_SUCCESS;
    }
}
