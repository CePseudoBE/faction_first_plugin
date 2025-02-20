package be.cepseudo.first_plugin;

import be.cepseudo.first_plugin.commands.CreateFactionCommand;
import be.cepseudo.first_plugin.commands.DisbandFactionCommand;
import be.cepseudo.first_plugin.commands.ShowFactionCommand;
import be.cepseudo.first_plugin.manager.FactionManager;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class FirstPlugin extends JavaPlugin {
    private FactionManager factionManager;

    @Override
    public void onEnable() {
        this.factionManager = new FactionManager();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(buildCommand());
        });
    }

    private LiteralCommandNode<CommandSourceStack> buildCommand() {
        // Création de la commande principale "/f"
        return LiteralArgumentBuilder.<CommandSourceStack>literal("f")
                .then(new CreateFactionCommand(factionManager).build()) // Ajout de /f create
                .then(new ShowFactionCommand(factionManager).build())   // Ajout de /f show
                .then(new DisbandFactionCommand(factionManager).build())
                .build();
    }
}
