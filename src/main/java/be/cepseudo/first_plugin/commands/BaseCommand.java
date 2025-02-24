package be.cepseudo.first_plugin.commands;

import be.cepseudo.first_plugin.utils.CommandUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public abstract class BaseCommand {
    public abstract LiteralArgumentBuilder<CommandSourceStack> build();

    protected Player getPlayerOrSendError(CommandContext<CommandSourceStack> context) {
        return CommandUtils.getPlayerOrSendError(context);
    }

    protected void sendMessage(Player player, String message) {
        CommandUtils.sendMessage(player, message);
    }

    protected void sendMessage(Player player, Messages message) {
        CommandUtils.sendMessage(player, message.get());
    }
}
