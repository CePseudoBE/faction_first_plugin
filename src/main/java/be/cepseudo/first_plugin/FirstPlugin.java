package be.cepseudo.first_plugin;

import be.cepseudo.first_plugin.commands.*;
import be.cepseudo.first_plugin.listeners.PlayerJoinListener;
import be.cepseudo.first_plugin.manager.FactionManager;
import be.cepseudo.first_plugin.manager.PlayerManager;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class FirstPlugin extends JavaPlugin {
    private FactionManager factionManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        factionManager = new FactionManager();
        playerManager = new PlayerManager();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(playerManager), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(buildCommand());
        });
    }

    private LiteralCommandNode<CommandSourceStack> buildCommand() {
        // Création de la commande principale "/f"
        return LiteralArgumentBuilder.<CommandSourceStack>literal("f")
                .then(new CreateFactionCommand(factionManager).build()) // Ajout de /f create
                .then(new ShowFactionCommand(factionManager).build())   // Ajout de /f show
                .then(new DisbandFactionCommand(factionManager).build()) // Ajout de /f disband
                .then(new LeaveFactionCommand(factionManager).build()) // Ajout de /f leave
                .then(new InvitInFactionCommand(factionManager, playerManager).build())
                .then(new JoinFactionCommand(factionManager, playerManager).build())
                .build();
    }
}
